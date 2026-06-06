import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { DayOfWeek } from 'src/app/core/models/businessDays.model';
import { FullMedicalAppointment } from 'src/app/core/models/full-medical-appointment.model';
import { MedicalAppointment, MedicalAppointmentStateEnum } from 'src/app/core/models/medical-appointment.model';
import { MedicalRecord } from 'src/app/core/models/medical-record.model';
import { MedicalAppointmentDataAllowedToUpdateDto } from 'src/app/core/models/MedicalAppointmentDataAllowedToUpdateDto.model';
import { Patient } from 'src/app/core/models/patient.model';
import { Professional } from 'src/app/core/models/professional.model';
import { Shift } from 'src/app/core/models/shift.model';
import { Speciality } from 'src/app/core/models/speciality.model';
import { RoleEnum, User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { MedicalAppointmentService } from 'src/app/core/services/medical-appointment.service';
import { PatientService } from 'src/app/core/services/patient.service';
import { ProfessionalService } from 'src/app/core/services/professional.service';
import { SpecialityService } from 'src/app/core/services/speciality.service';
import { UserService } from 'src/app/core/services/user.service';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { VerifyDialogComponent } from 'src/app/shared/verify-dialog/verify-dialog.component';

@Component({
  selector: 'app-secretary-view',
  templateUrl: './secretary-view.component.html',
  styleUrls: ['./secretary-view.component.css']
})
export class SecretaryViewComponent implements OnInit {
  pacients: Patient[] = [];
  filetredPatients: Patient[] = [];
  filteredAppointmentsList: FullMedicalAppointment[] = [];
  isLoading: boolean = true;
  errorMessage: string | null = null;
  editingDni: string | null = null;
  editedPatient: Patient | null = null;
  today = new Date();
  showMedicalHistory = false;
  medicalHistory: MedicalRecord[] = [];
  selectedPatient: Patient | null = null;
  showAddModal = false;
  showAppointments: boolean = false;
  selectedPatientForAppointments?: Patient;
  medicalAppoinmentList: FullMedicalAppointment[] = [];
  showAddAppointmentModal = false;
  selectedPatientDNI?: string;
  specialities: Speciality[] = [];
  selectedSpeciality: string | null = null;
  selectedProfessional: Professional | null = null;
  selectedDate: Date | null = null;
  buttonGoBackValidator: boolean = false;
  buttonAddAppointment: boolean = false;
  selectedAppointment: FullMedicalAppointment | null = null;
  showRescheduleModal: boolean = false;
  availableAppointments: Shift[] = [];
  availableDays: string[] = [];
  selectedState: MedicalAppointmentStateEnum | null = null;
  appointmentStates = Object.values(MedicalAppointmentStateEnum);
  professionals:User[] = [];
  sortColumn: string | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';
  showAllAppointments: boolean = false;
  lastname?: string;

  patientSearchQuery: string = '';
  selectedPatientState: string = 'ACTIVO';
  appointmentSearchQuery: string = '';

  constructor(private _patientService: PatientService,
    private _specialityService: SpecialityService,
    private _authService: AuthService,
    private _userService: UserService,
    private router: Router,
    private _medicalAppService: MedicalAppointmentService,
    private dialog: MatDialog,
    private _professionalService: ProfessionalService
  ) { }

  ngOnInit(): void {
    this.selectedPatientState = 'ACTIVO';
    this.loadPatients();
    this.loadSpecialities();
    this.validateButtons();
    this.validateAccount();
    this.loadProfessionals();
  }

  private openDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
    username: string
  ): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { username },
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: true,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
    dialogRef.afterClosed().subscribe((data) => {
      if (data === undefined) {
        this.updatePassword(username, data);
      }
      else {
        this.updatePassword(username, data);
      }
    })
  }

  sortData(column: keyof FullMedicalAppointment): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    this.filteredAppointmentsList.sort((a, b) => {
      const valueA: any = a[column];
      const valueB: any = b[column];

      if (column === 'time') {
        return this.sortDirection === 'asc' ? this.convertTimeToMinutes(valueA) - this.convertTimeToMinutes(valueB) : this.convertTimeToMinutes(valueB) - this.convertTimeToMinutes(valueA);
      } else if (column === 'date') {
        return this.sortDirection === 'asc' ? new Date(valueA).getTime() - new Date(valueB).getTime() : new Date(valueB).getTime() - new Date(valueA).getTime();
      } else { 
        if (valueA < valueB) return this.sortDirection === 'asc' ? -1 : 1;
        if (valueA > valueB) return this.sortDirection === 'asc' ? 1 : -1;
        return 0; 
      }
    });
  }

  private convertTimeToMinutes(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes; 
  }

  loadProfessionals()
  {
    this._userService.getUsersByRole('PROFESSIONAL').subscribe({
      next: (professional) => {
       this.professionals = professional;
       console.log(this.professionals);
      },
      error: (err) => this.handleError('Error cargando turnos disponibles', err)
    });
  }

  onDateChange(event: any): void {
    this.selectedDate = new Date(event.target.value); 
  }


  applyFilters() {
    let params = new HttpParams();
  
    if (this.selectedProfessional !== null && this.selectedProfessional !== undefined) {
      params = params.set('professionalDni', this.selectedProfessional.dni);
    }
    if (this.selectedSpeciality) {
      params = params.set('specialityName', this.selectedSpeciality);
    }
    if (this.selectedDate) {
      try {
        params = params.set('selectedDate', this.selectedDate.toISOString().split('T')[0]);
      } catch (error) {
        this.selectedDate = null;
      }
    }
   
    this._medicalAppService.getAppointmentByFilters(params).subscribe(
      appointments => {
        this.medicalAppoinmentList = appointments;
        this.filterAppointments();
      },
      error => {
        console.error("Error cargando turnos disponibles:", error);
      }
    );
  }
  

  updatePassword(username: string, newPassword: string) {
    if (newPassword === '' || newPassword === undefined) {
      this.openDialog("200ms", "200ms", username);
    }
    this._userService.updatePassword(username, newPassword).subscribe({
      next: () => {
        alert('Contraseña de usuario ' + username + ' actualizada con exito');
        this.ngOnInit();
      },
      error: () => {
        const errorMessage = "Debe ingresar una contraseña valida";
        alert(errorMessage);
      },
    });
  }

  openRescheduleModal(appointment: FullMedicalAppointment): void {
    this.selectedAppointment = appointment;
    this.showRescheduleModal = true;
    this.loadAvailableAppointments(appointment.professionalDni);
  }

  loadAvailableAppointments(professionalDni: string): void {
    this._professionalService.getProfessionalByDni(professionalDni).subscribe({
      next: (professional: Professional) => {
        if (!professional.businessDays) {
          this.availableAppointments = [];
          return;
        }

        const availableShifts = professional.businessDays.flatMap(bd =>
          bd.shifts
            .filter(shift => !shift.isShiftReserved)
            .map(shift => ({
              ...shift,
              businessDays: bd
            }))
        );

        this.availableAppointments = availableShifts;
        this.generateAvailableDays();
      },
      error: (err) => this.handleError('Error cargando turnos disponibles', err)
    });
  }


  generateAvailableDays(): void {
    const daysSet = new Set<string>();
    this.availableAppointments.forEach(shift => {
      if (shift.businessDays && shift.businessDays.dayOfWeek) {
        daysSet.add(shift.businessDays.dayOfWeek.toUpperCase());
      }
    });


    const dayOrder = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
    this.availableDays = Array.from(daysSet)
      .map(day => day.trim().toUpperCase())  
      .filter(day => dayOrder.includes(day)) 
      .sort((a, b) => {
        const aIndex = dayOrder.indexOf(a);
        const bIndex = dayOrder.indexOf(b);


        return aIndex - bIndex;  
      });


  }


  rescheduleAppointment(event: { newDate: string; newTime: string }): void {
    if (this.selectedAppointment) {
      const updateData: MedicalAppointmentDataAllowedToUpdateDto = {
        date: event.newDate,
        time: event.newTime
      };

      this._medicalAppService.rescheduleAppointment(
        this.selectedAppointment.medicalAppointmentId,
        updateData
      ).subscribe({
        next: () => {
          alert('Turno reprogramado correctamente');
          this.loadMedicalAppointments(this.selectedAppointment!.patientDni); // Recargar lista
          this.showRescheduleModal = false; // Cerrar el modal
          this.selectedAppointment = null; // Reiniciar selección
        },
        error: (err) => this.handleError('Error al reprogramar turno', err)
      });
    }
  }

  getStateDisplayName(state: string): string {
    return state.toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase())
      .replace('_', ' ');
  }

  getfilteredAppointments(): FullMedicalAppointment[] {
    return this.medicalAppoinmentList.filter(app => {
      const matchesState = !this.selectedState || app.state === this.selectedState;
      return matchesState;
    });
  }
  onStateFilterChange(): void {
    if (this.selectedPatient) {
      if (this.selectedState) {
        this._medicalAppService.getMedicalAppointmentsByState(this.selectedState).subscribe({
          next: (appointments: FullMedicalAppointment[]) => {
            this.medicalAppoinmentList = appointments.filter(app => app.patientDni === this.selectedPatient!.dni);
            this.filterAppointments();
          },
          error: (err) => this.handleError('Error cargando turnos', err)
        });
      } else {
        this.loadMedicalAppointments(this.selectedPatient.dni);
      }
    } else {
      this.filterAppointments();
    }
  }

  cancelAppointment(id: number, dni: string) {
    this._medicalAppService.cancelAppointment(id).subscribe({
      next: () => {
        alert("Cita cancelada correctamente");
        this.loadMedicalAppointments(dni);

      },
      error: (err) => this.handleError('Error al cancelar la cita', err)
    });
  }

  private validateButtons() {
    const token = this._authService.getInfoToken();

    this._userService.getUserByUsername(token.sub).subscribe({
      next: (response) => {
        if (response.role === RoleEnum.ADMIN) {
          this.buttonGoBackValidator = true;
          this.buttonAddAppointment = true;
        }
      }
    })
  }

  validateAccount() {
    const token = this._authService.getInfoToken();

    this._userService.getUserByUsername(token.sub).subscribe({
      next: (response) => {
        if (response.role !== 'ADMIN' && response.newAccount === true) {
          this.openDialog("200ms", "200ms", response.username);
        }
      }
    })
  }

  returnToAdminView() {
    this.router.navigate(['/admin']);
  }



  onPatientSearch(event: any) {
    this.patientSearchQuery = event.target.value;
    this.filterPatients();
  }

  loadPatients(): void {
    this._patientService.getPatients().subscribe({
      next: (data: Patient[]) => {
        this.pacients = data;
        this.filterPatients();
      },
      error: (err) => this.handleError('Error cargando pacientes', err)
    });
  }

  filterPatients(): void {
    let temp = [...this.pacients];

    // Filter by DNI, Name, or Lastname
    if (this.patientSearchQuery && this.patientSearchQuery.trim() !== '') {
      const q = this.patientSearchQuery.toLowerCase().trim();
      temp = temp.filter(p => 
        (p.dni && p.dni.toLowerCase().includes(q)) ||
        (p.name && p.name.toLowerCase().includes(q)) ||
        (p.lastname && p.lastname.toLowerCase().includes(q))
      );
    }

    // Filter by Active status
    if (this.selectedPatientState === 'ACTIVO') {
      temp = temp.filter(p => p.active === true);
    } else if (this.selectedPatientState === 'INACTIVO') {
      temp = temp.filter(p => p.active === false);
    }

    this.filetredPatients = temp;
  }

  historialDetail(dni: string): void {
    this._patientService.getMedicalRecord(dni).subscribe({
      next: (data) => {

        this.medicalHistory = [data];
        this.selectedPatient = this.filetredPatients.find(p => p.dni === dni) || null;
        this.showMedicalHistory = true;
        this.errorMessage = '';
      },
      error: (err) => {
        console.error('Error fetching medical history:', err);
        this.errorMessage = 'Error al obtener el historial médico';
        this.showMedicalHistory = false;
      }
    });
  }
  goBack() {
    this.showMedicalHistory = false;
    this.medicalHistory = [];
    this.selectedPatient = null;
    this.showAppointments = false;
    this.showAllAppointments= false;
  }
  getFormattedTime(time: string): string {
    return time.split(':').slice(0, 2).join(':'); // Extrae HH:mm
  }


  updateStatePacient(dni: string, newState: boolean): void {
    this._patientService.updatePatientState(dni, newState).subscribe({
      next: () => {
        this.ngOnInit();
      },
      error: (err: any) => this.handleError('Error actualizando estado', err)
    });
  }

  private handleError(operacion: string, error: any): void {
    console.error(`${operacion}:`, error);

    if (error.error && typeof error.error === 'object') {
      this.errorMessage = Object.values(error.error).join(', ');
    } else {
      this.errorMessage = error.error?.message || 'Ocurrió un error inesperado';
    }

    setTimeout(() => this.errorMessage = null, 5000);
  }

  private showSuccess(mensaje: string): void {
    // Implementar lógica para mostrar mensaje de éxito
    console.log(mensaje);
  }

  addPatient(): void {
    console.log('Mostrando modal', this.showAddModal);
    this.showAddModal = true;

  }

  onPatientAdded(): void {
    this.loadPatients();
  }
  closeAddModal(): void {
    this.showAddModal = false;
  }
  loadSpecialities(): void {
    this._specialityService.getSpecialities().subscribe({
      next: (res) => this.specialities = res,
      error: (err) => console.error('Error cargando especialidades', err)
    });
  }
  loadMedicalAppointments(dni: string): void {
    if (this.selectedSpeciality) {
      this._patientService.getMedicalAppointmentsByPatientAndSpeciality(this.selectedSpeciality, dni)
        .subscribe({
          next: (data) => {
            this.medicalAppoinmentList = data;
            this.filterAppointments();
          },
          error: (err) => this.handleError('Error cargando turnos', err)
        });
    } else {
      this._patientService.getMedicalAppointmentsByPatient(dni).subscribe({
        next: (data) => {
          this.medicalAppoinmentList = data;
          this.filterAppointments();
        },
        error: (err) => this.handleError('Error cargando turnos', err)
      });
    }
  }

  onSpecialityFilterChange(): void {
    if (this.selectedPatient) {
      this.loadMedicalAppointments(this.selectedPatient.dni);
    }
  }

  medicalAppoinmentDetail(patient: Patient) {
    this.selectedPatient = patient;
    this.showAppointments = true;
    this.loadMedicalAppointments(patient.dni)
  }

  addMedicalAppoinment(): void {
    if (this.selectedPatient) {
      this.showAddAppointmentModal = true;
    }
  }

  createMedicalAppointment(appointmentData: any): void {
    const newAppointment: MedicalAppointment = {
      ...appointmentData,
      patientDNI: this.selectedPatient?.dni!,
      estado: 'PENDIENTE'
    };

    this._patientService.createMedicalAppointment(newAppointment).subscribe({
      next: (response) => {
        console.log('Turno creado correctamente:', response);
        this.loadMedicalAppointments(this.selectedPatient?.dni!);
        this.showAddAppointmentModal = false;
      },
      error: (err) => this.handleError('Error creando turno', err)
    });
  }

  modifyPatient(paciente: Patient): void {
    this.editingDni = paciente.dni;
    this.editedPatient = { ...paciente };
  }

  saveChanges(): void {
    if (this.editingDni && this.editedPatient) {
      const pacienteActualizado: Patient = {
        ...this.editedPatient
      };

      this._patientService.updatePatient(this.editingDni, pacienteActualizado).subscribe({
        next: (updatedPatient) => {
          const index = this.pacients.findIndex(p => p.dni === this.editingDni);
          if (index !== -1) {
            this.pacients[index] = { ...updatedPatient };
            this.filterPatients();
          }
          this.salirModoEdicion();
          this.showSuccess('Paciente actualizado correctamente');
          this.loadPatients();
        },
        error: (err) => {
          this.handleError('Error actualizando paciente', err);
          this.loadPatients();
        }
      });
    }
  }

  isValidPatient(): boolean {
    return !!this.editedPatient?.name?.trim() &&
      !!this.editedPatient?.lastname?.trim() &&
      !!this.editedPatient?.email?.trim() &&
      !!this.editedPatient?.phoneNumber?.trim() &&
      !!this.editedPatient?.address?.trim() &&
      !!this.editedPatient?.birthdate;
  }

  salirModoEdicion(): void {
    this.editingDni = null;
    this.editedPatient = null;
  }

  onAppointmentSearch(event: any) {
    this.appointmentSearchQuery = event.target.value;
    this.filterAppointments();
  }

  filterAppointments(): void {
    let temp = [...this.medicalAppoinmentList];

    if (this.appointmentSearchQuery && this.appointmentSearchQuery.trim() !== '') {
      const q = this.appointmentSearchQuery.toLowerCase().trim();
      temp = temp.filter(app => 
        (app.patientName && app.patientName.toLowerCase().includes(q)) ||
        (app.patientLastname && app.patientLastname.toLowerCase().includes(q)) ||
        (app.patientDni && app.patientDni.toLowerCase().includes(q)) ||
        (app.professionalName && app.professionalName.toLowerCase().includes(q)) ||
        (app.professionalLastname && app.professionalLastname.toLowerCase().includes(q)) ||
        (app.specialityName && app.specialityName.toLowerCase().includes(q))
      );
    }

    if (this.selectedState) {
      temp = temp.filter(app => app.state === this.selectedState);
    }

    this.filteredAppointmentsList = temp;
  }

  public viewAllAppointments(): void {
    this.showAllAppointments = true;
    this.showAppointments = false;
    this._medicalAppService.getAllMedicalAppointments().subscribe({
      next: (appointments) => {
        this.medicalAppoinmentList = appointments;
        this.filterAppointments();
      },
      error: (err) => {
        this.handleError('Error cargando todos los turnos', err);
      }
    });
  }
  


}

