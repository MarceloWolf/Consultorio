import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Consultation } from 'src/app/core/models/consultation.model';
import { FullMedicalAppointment } from 'src/app/core/models/full-medical-appointment.model';
import { MedicalRecord } from 'src/app/core/models/medical-record.model';
import { Patient } from 'src/app/core/models/patient.model';
import { RoleEnum } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { ConsultationService } from 'src/app/core/services/consultation.service';
import { MedicalAppointmentService } from 'src/app/core/services/medical-appointment.service';
import { MedicalRecordService } from 'src/app/core/services/medical-record.service';
import { PatientService } from 'src/app/core/services/patient.service';
import { ProfessionalService } from 'src/app/core/services/professional.service';
import { UserService } from 'src/app/core/services/user.service';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { AddConsultationComponent } from '../add-consultation/add-consultation.component';
import { Speciality } from 'src/app/core/models/speciality.model';
import { SpecialityService } from 'src/app/core/services/speciality.service';
import { MedicalAppointment } from 'src/app/core/models/medical-appointment.model';
import { ConsultationDetailComponent } from '../consultation-detail/consultation-detail.component';

@Component({
  selector: 'app-professional-view',
  templateUrl: './professional-view.component.html',
  styleUrls: ['./professional-view.component.css']
})
export class ProfessionalViewComponent implements OnInit {
  allAppointments: FullMedicalAppointment[] = [];
  filteredAppointments: FullMedicalAppointment[] = [];
  medicalHistory: MedicalRecord [] =[];
  selectedPatient: Patient | null = null;
  showMedicalHistory = false;
  showPatientDetail=false;
  errorMessage: string | null = null;
  buttonGoBackValidator:boolean = false;
  editingRecordDni: string | null = null;
  editedMedicalRecord: MedicalRecord | null = null;
  showConsultations = false;
  selectedMedicalRecordId: number | null = null;
  consultations: Consultation[] = [];
  selectedAppointment: FullMedicalAppointment | null = null;
  specialities: Speciality[] = [];
  medicalAppoinmentList: FullMedicalAppointment[]= [];
  selectedSpeciality:string | null = null;
  appointmens: FullMedicalAppointment[] = [];
  selectedDate: Date | null = null;
  appointmentsOnSelectedDate: FullMedicalAppointment[] = [];
  sortColumn: string | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';



  filtrosForm = new FormGroup({
    dniBusqueda: new FormControl('', [Validators.pattern('^[0-9]{7,8}$')]),
    filtroEstado: new FormControl(null),
    filtroEspecialidad: new FormControl(null) 
  });

  constructor(private _medicalAppointmentService: MedicalAppointmentService,private _authService:AuthService, 
      private _userService:UserService,
      private _patientService:PatientService,
      private _medicalAppService:MedicalAppointmentService,
      private _professionalService: ProfessionalService,
      private _medicalRecordService: MedicalRecordService,
      private _consultationService: ConsultationService,
      private _specialityService: SpecialityService,
      private router:Router,
      private dialog:MatDialog) {}

  ngOnInit(): void {
    this.loadAppointments();
    this.validateAccount();
    this.validateRoleToGoBack();
    this.loadSpecialities();
    this.loadProfessionalSpecialities();
  }

  private openDialog(
        enterAnimationDuration: string,
        exitAnimationDuration: string,
        username:string
      ): void {
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
          data:{username},
          width: '400px',
          enterAnimationDuration,
          exitAnimationDuration,
          hasBackdrop: true,
          disableClose: true, 
          panelClass: 'custom-dialog-panel',
          backdropClass: 'cdk-overlay-dark-backdrop',
        });
        dialogRef.afterClosed().subscribe((data) => {
          if(data === undefined)
          {
            this.updatePassword(username,data);
          }
          else
          {
              this.updatePassword(username,data);
          }
        })
      }

  validateAccount()
  {
    const token = this._authService.getInfoToken();

    this._userService.getUserByUsername(token.sub).subscribe({
      next:(response) => {
        if(response.role !== 'ADMIN' && response.newAccount === true)
        {
          this.openDialog("200ms","200ms",response.username);
        }
      }
    })
  }
  
      updatePassword(username:string ,newPassword:string) {
        if(newPassword === '' || newPassword === undefined)
          {
            this.openDialog("200ms","200ms",username);
          }
        this._userService.updatePassword(username,newPassword).subscribe({
          next: () => {
            alert('Contraseña de usuario ' + username + ' actualizada con exito');
            this.ngOnInit();
          },
          error: (err) => {
            const errorMessage = "Debe ingresar una contraseña valida";
            alert(errorMessage);
          },
        });
      }
  

  private validateRoleToGoBack()
    {
      const token = this._authService.getInfoToken();
  
      this._userService.getUserByUsername(token.sub).subscribe({
        next:(response) => {
          if(response.role === RoleEnum.ADMIN)
          {
            this.buttonGoBackValidator = true;
          }
        }
      })
    }
  
    returnToAdminView()
    {
      this.router.navigate(['/admin']);
    }

    loadAppointments(): void {
      const token = this._authService.getInfoToken();
    
      this._userService.getUserByUsername(token.sub).subscribe({
        next: (response) => {
          let professionalDni = '';
          if (response.role === RoleEnum.PROFESSIONAL) {
            professionalDni = response.dni;
          }
          this._medicalAppointmentService.getMedicalAppointmentsByProfessional(professionalDni).subscribe({
            next: (data) => {
              this.allAppointments = data;
              this.applyFilters();
            },
            error: (err) => this.handleError('Error cargando las citas', err)
          });
        },
        error: (err) => this.handleError('Error obteniendo información del profesional', err)
      });
    }

    applyFilters(): void {
      const dni = this.filtrosForm.value.dniBusqueda;
      const estado = this.filtrosForm.value.filtroEstado;
      if (dni) {
        // Si se ingresa un DNI, primero obtener el paciente
        this._patientService.getPatientByDni(dni).subscribe(patient => {
          console.log('Paciente encontrado:', patient);
          
          // Filtrar todas las citas por el DNI del paciente
          this._medicalAppService.getMedicalAppointmentsByState(estado!).subscribe(appointments => {
            this.filteredAppointments = appointments.filter(appointment => appointment.patientDni === patient.dni);
            console.log('Citas filtradas por paciente y estado:', this.filteredAppointments);
          }, error => {
            console.error('Error al obtener citas médicas por estado:', error);
          });
          
        }, error => {
          console.error('Error al obtener paciente por DNI:', error);
        });
    
      } else {
        // Si no hay un DNI, solo filtrar por estado
        if (estado) {
          this._medicalAppService.getMedicalAppointmentsByState(estado).subscribe(appointments => {
            this.filteredAppointments = appointments;
            console.log('Citas filtradas solo por estado:', this.filteredAppointments);
          }, error => {
            console.error('Error al obtener citas médicas por estado:', error);
          });
        } else {

          this.filteredAppointments = [...this.allAppointments];
          console.log('Todas las citas:', this.filteredAppointments);
        }
      }
    }

  findPatient(): void {
    this.applyFilters();
  }


  viewPatientDetail(appointment: FullMedicalAppointment): void {
    this.selectedAppointment = appointment;
    this._patientService.getPatientByDni(this.selectedAppointment.patientDni).subscribe({
      next: (patient) => {
        this.selectedPatient = patient;
        this.showPatientDetail = true;
        this.showMedicalHistory = false;
        console.log(this.selectedPatient)
        console.log(this.showPatientDetail)
      },
      error: (err) => this.handleError('Error obteniendo paciente', err)
    });
  }
  
  viewMedicalRecords(dni: string): void {
    this._professionalService.getMedicalRecordByPatientDni(dni).subscribe({
      next: (response: any) => {
        this.medicalHistory = Array.isArray(response) ? response: [response];
        console.log(this.medicalHistory)
        this.showMedicalHistory = true; // Muestra la sección de historial médico
      },
      error: (err) => this.handleError('Error cargando historial médico', err)
    });
  }
  
  modifyMedicalRecord(record: MedicalRecord): void {
    this.editingRecordDni = record.patient.dni;
    this.editedMedicalRecord = { ...record };
  }

  saveMedicalRecordChanges(): void {
    if (this.editingRecordDni && this.editedMedicalRecord) {
      this._medicalRecordService.updateMedicalRecord(
        this.editingRecordDni, 
        this.editedMedicalRecord
      ).subscribe({
        next: (updatedRecord) => {
          const index = this.medicalHistory!.findIndex(r => 
            r.patient.dni === this.editingRecordDni
          );
          
          if (index !== -1) {
            this.medicalHistory[index] = updatedRecord;
          }
          this.viewMedicalRecords(this.editingRecordDni!);
          this.cancelMedicalRecordEdit();
        },
        error: (err) => this.handleError('Error al actualizar', err)
      });
    }
  }

  cancelMedicalRecordEdit(): void {
    this.editingRecordDni = null;
    this.editedMedicalRecord = null;
  }
  isMedicalRecordValid(): boolean {
    return !!this.editedMedicalRecord?.description?.trim() && 
          !!this.editedMedicalRecord?.date;
  }


  goBack(): void {
    this.showMedicalHistory = false;
    this.showPatientDetail=false;
    this.selectedPatient = null;
    this.medicalHistory = [];
  }

  goBackPatient(): void{
    this.showMedicalHistory = false;
    this.medicalHistory= [];
  }


  viewConsultationPatient(medicalRecordId: number): void {
    this.selectedMedicalRecordId = medicalRecordId;
    this._consultationService.getConsultationByMedicalRecordId(medicalRecordId).subscribe({
      next: (consultations) => {
        console.log(consultations)
        this.consultations = consultations;
        this.showConsultations = true;
      },
      error: (err) => this.handleError('Error cargando consultas', err)
    });
  }

  openAddConsultationModal(): void {
    const token = this._authService.getInfoToken();
  
    this._userService.getUserByUsername(token.sub).subscribe({
      next: (response) => {
        if (response.role === RoleEnum.PROFESSIONAL && this.selectedAppointment) {
        
          if (!this.isAppointmentToday(this.selectedAppointment.date)) {
            this.errorMessage = 'Solo puedes agregar consultas el día del turno';
            setTimeout(() => this.errorMessage = null, 5000);
            return;
          }
          const dialogRef = this.dialog.open(AddConsultationComponent, {
            width: '600px',
            data: {
              professionalDni: response.dni,
              patientDni: this.selectedPatient?.dni,
              specialityName: this.selectedAppointment.specialityName,
              appointmentDate: this.selectedAppointment.date,
              appointmentTime: this.selectedAppointment.time.slice(0,5)
            }
          });
  
          dialogRef.afterClosed().subscribe(result => {
            if (result) {
              this.viewConsultationPatient(this.selectedMedicalRecordId!);
            }
          });
        }
      },
      error: (err) => this.handleError('Error obteniendo información del profesional', err)
    });
  }

  private isAppointmentToday(appointmentDate: string): boolean {
    const appointment = new Date(appointmentDate + 'T00:00:00');
    const today = new Date();
    console.log(appointment, today);
    
  
    return (
      appointment.getFullYear() === today.getFullYear() &&
      appointment.getMonth() === today.getMonth() &&
      appointment.getDate() === today.getDate()
    );
  }
  private loadProfessionalSpeciality(): void {
    const token = this._authService.getInfoToken();
    if (token && token.speciality) {
      this.loadProfessionalSpeciality = token.speciality;
    }
  }


  loadSpecialities(): void {
    this._specialityService.getSpecialities().subscribe({
      next: (res) => this.specialities = res,
      error: (err) => console.error('Error cargando especialidades', err)
    });
  }

  onSpecialityChange(event: any): void {
    const selectedSpeciality = event.target.value;
  
    if (selectedSpeciality) {
      this._consultationService.getConsultationBySpeciality(selectedSpeciality).subscribe({
        next: (consultations) => {
          this.consultations = consultations;
        },
        error: (err) => this.handleError('Error cargando consultas por especialidad', err)
      });
    } else {
      this.consultations = [];
    }
  }

  onStateFilterChange(event: any): void {
    const selectedState = event.target.value;
  
    if (selectedState) {
      const token = this._authService.getInfoToken();
      const username = token.sub;
  
      this._userService.getUserByUsername(username).subscribe({
        next: (user) => {
          const professionalDni = user.dni;
  
          this._medicalAppointmentService.getMedicalAppointmentsByProfessional(professionalDni).subscribe({
            next: (appointments) => {
              this.filteredAppointments = appointments.filter(appointment => {
                return appointment.state === selectedState; 
              });
  
              console.log('Citas médicas filtradas por estado y profesional:', this.filteredAppointments);
            },
            error: (err) => this.handleError('Error cargando citas médicas del profesional', err)
          });
        },
        error: (err) => this.handleError('Error obteniendo información del profesional', err)
      });
    } else {
      this.filteredAppointments = []; 
    }
  }

  loadProfessionalSpecialities(): void {
    const token = this._authService.getInfoToken();
    this._userService.getUserByUsername(token.sub).subscribe({
      next: (user) => {
        const professionalDni = user.dni;
  
        this._specialityService.getSpecialitiesByProfessional(professionalDni).subscribe({
          next: (res) => {
            this.specialities = res;
            
          },
          error: (err) => console.error('Error cargando especialidades del profesional', err)
        });
      }
    });
  }

  searchBySpeciality(event: Event): void {
    const target = event.target as HTMLSelectElement; 
    this.selectedSpeciality = target.value; 
    
    
    if (this.selectedSpeciality) {
      const token = this._authService.getInfoToken();
      const username = token.sub;
  
      this._userService.getUserByUsername(username).subscribe({
        next: (user) => {
          const professionalDni = user.dni;
          
          if (this.selectedDate) {
            this.filteredAppointments = this.appointmentsOnSelectedDate.filter(appointment => 
               appointment.specialityName === this.selectedSpeciality && appointment.professionalDni === professionalDni
            );
            if (this.filteredAppointments.length === 0) {
              this.errorMessage = 'No se encontraron turnos para la especialidad seleccionada';
              setTimeout(() => this.errorMessage = null, 5000);
            } else {
              this.errorMessage = null;
            }
          } else {
            this._medicalAppService.getMedicalAppointmentsByProfessional(professionalDni).subscribe({
              next: (consultations) => {
                console.log('Consultas',consultations);
                
                this.filteredAppointments = consultations.filter(consultation => consultation.specialityName === this.selectedSpeciality);
                console.log(this.filteredAppointments);
                
                
                if (this.filteredAppointments.length === 0) {
                  this.errorMessage = 'No se encontraron turnos para la especialidad seleccionada';
                  setTimeout(() => this.errorMessage = null, 5000);
                } else {
                  this.errorMessage = null;
                }
              },
              error: (err) => this.handleError('Error cargando consultas', err)
            });
          }
        },
        error: (err) => this.handleError('Error obteniendo información del profesional', err)
      });
    } else {
      this.filteredAppointments = [];
      this.errorMessage = 'Por favor selecciona una especialidad';
      setTimeout(() => this.errorMessage = null, 5000);
    }
  }
  
  
  onDateChange(event: any): void {
    this.selectedDate = new Date(event.target.value); // Almacena la fecha seleccionada
  }
  
  fetchAppointmentsByDate(): void {
    let formattedDate: string | null = null;
    try {
      if (this.selectedDate instanceof Date && !isNaN(this.selectedDate.getTime())) {
          formattedDate = this.selectedDate.toISOString().split('T')[0];
      } else {
        this.selectedDate = null;
      }
    } catch (error) {
      this.selectedDate = null;
    }
    if (formattedDate) {
      
      this._medicalAppointmentService.getAllMedicalAppointmentByDate(formattedDate).subscribe({
        next: (appointments) => {
          this.appointmentsOnSelectedDate = appointments;
          this.filteredAppointments = appointments;
          
          if (this.filteredAppointments.length === 0) {
            this.errorMessage = 'No se encontraron turnos para la fecha seleccionada';
            setTimeout(() => this.errorMessage = null, 5000);
          } else {
            this.errorMessage = null;
          }
        },
        error: (err) => this.handleError('Error cargando citas por fecha', err)
      });
    } else {
      this.loadAppointments();
    }
  }
  

  openConsultationDetail(consultation: Consultation): void {
    this.dialog.open(ConsultationDetailComponent, {
      width: '600px',
      data: consultation
    });
  }


  sortData(column: keyof FullMedicalAppointment): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
  
    this.filteredAppointments.sort((a, b) => {
      const valueA: any = a[column];
      const valueB: any = b[column];

      if (column === 'time') {
        return this.sortDirection === 'asc' ? this.convertTimeToMinutes(valueA) - this.convertTimeToMinutes(valueB) : this.convertTimeToMinutes(valueB) - this.convertTimeToMinutes(valueA);
      } else if (column === 'date') {
        return this.sortDirection === 'asc' ? new Date(valueA).getTime() - new Date(valueB).getTime() : new Date(valueB).getTime() - new Date(valueA).getTime();
      } else { 
        // Para los demás campos, hacer comparación normal
        if (valueA < valueB) return this.sortDirection === 'asc' ? -1 : 1;
        if (valueA > valueB) return this.sortDirection === 'asc' ? 1 : -1;
        return 0; // Son iguales
      }
    });
  }
  private convertTimeToMinutes(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes; // Convertir tiempo a minutos para comparación numérica
  }

  private handleError(context: string, error: any): void {
    console.error(`${context}:`, error);
    this.errorMessage = error.error?.message || 'Error en la operación';
    setTimeout(() => this.errorMessage = null, 5000);
  }


}
