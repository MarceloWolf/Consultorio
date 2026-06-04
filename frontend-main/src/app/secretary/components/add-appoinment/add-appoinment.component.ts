import { WeekDay } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DayOfWeek } from 'src/app/core/models/businessDays.model';
import { Professional } from 'src/app/core/models/professional.model';
import { Speciality } from 'src/app/core/models/speciality.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { PatientService } from 'src/app/core/services/patient.service';
import { SpecialityService } from 'src/app/core/services/speciality.service';
import { UserService } from 'src/app/core/services/user.service';
import { VerifyDialogComponent } from 'src/app/shared/verify-dialog/verify-dialog.component';

@Component({
  selector: 'app-add-appoinment',
  templateUrl: './add-appoinment.component.html',
  styleUrls: ['./add-appoinment.component.css']
})
export class AddAppoinmentComponent {
  @Output() appointmentCreated = new EventEmitter<any>();
  @Output() closed = new EventEmitter<void>();
  @Input() patientDni: string = '';
  
  specialities: Speciality[] = [];
  professionals: Professional[] = [];
  selectedProfessional?: Professional;
  availableShiftsByWeek: { weekLabel: string; shifts: { date: string; time: string }[] }[] = [];
  selectedShift?: { date: string; time: string };
  isShiftModalVisible: boolean = false;

  appointmentForm = new FormGroup({
    patientDni: new FormControl('', Validators.required),
    speciality: new FormControl('', Validators.required),
    professional: new FormControl({value: '', disabled: true}, Validators.required),
    shift: new FormControl({value: '', disabled: true}, Validators.required)
  });

  constructor(
    private _patientService: PatientService,
    private _authService: AuthService,
    private _userService: UserService,
    private _specialityService: SpecialityService,
    private _dialog: MatDialog,
  ) {}

  ngOnChanges(): void {
    if (this.patientDni) {
      this.appointmentForm.patchValue({
        patientDni: this.patientDni
      });
      this.appointmentForm.get('patientDni')?.disable();
    }
  }

  ngOnInit(): void {
    this.loadSpecialities();
    if (this.patientDni) {
      this.appointmentForm.patchValue({
        patientDni: this.patientDni
      });
      this.appointmentForm.get('patientDni')?.disable();
    }
  }

  loadSpecialities(): void {
    this._specialityService.getSpecialities().subscribe({
      next: (res) => this.specialities = res,
      error: (err) => console.error('Error cargando especialidades', err)
    });
  }

  onSpecialityChange(): void {
    const specialityName = this.appointmentForm.get('speciality')?.value;
    if (specialityName) {
      this._patientService.getProfessionalsBySpeciality(specialityName).subscribe({
        next: (res) => {
          this.professionals = res;
          this.appointmentForm.get('professional')?.enable();
          this.appointmentForm.get('shift')?.disable();
        },
        error: (err) => console.error('Error cargando profesionales', err)
      });
    } else {
      this.appointmentForm.get('professional')?.disable();
      this.appointmentForm.get('shift')?.disable();
    }
  }

  onProfessionalChange(): void {
    const professionalDni = this.appointmentForm.get('professional')?.value;
    this.selectedProfessional = this.professionals.find(p => p.dni === professionalDni);
    
    if (this.selectedProfessional) {
      this.generateAvailableShifts();
      this.appointmentForm.get('shift')?.enable();
      this.isShiftModalVisible = true;
    } else {
      this.availableShiftsByWeek = [];
      this.appointmentForm.get('shift')?.disable();
    }
  }

  generateAvailableShifts(): void {
    const shifts: { date: string; time: string }[] = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Ajustarlo a medianoche para comparar sólo fechas
  
    this.selectedProfessional?.businessDays?.forEach(businessDay => {
      businessDay.shifts.forEach(shift => {
        const shiftDate = new Date(shift.shiftDate);
        if (!shift.isShiftReserved && shiftDate >= today) {
          shifts.push({
            date: shift.shiftDate,
            time: shift.shiftTime
          });
        }
      });
    });
    console.log("turnos sin ordenar", shifts);
    
  
    const groupedShifts = shifts.reduce((acc, shift) => {
      const date = new Date(shift.date + 'T00:00:00');
      const weekStart = this.getMonday(date);
      const weekKey = weekStart.toISOString().split('T')[0];

      if (!acc[weekKey]) {
        acc[weekKey] = {
          weekStart,
          shifts: []
        };
      }
      acc[weekKey].shifts.push(shift);
      return acc;
    }, {} as { [key: string]: { weekStart: Date; shifts: typeof shifts } });
  
    this.availableShiftsByWeek = Object.values(groupedShifts)
      .sort((a, b) => a.weekStart.getTime() - b.weekStart.getTime())
      .map(week => ({
        weekLabel: `Semana del ${this.formatDate(week.weekStart)}`,
        shifts: week.shifts.sort((a, b) =>
          new Date(a.date).getTime() - new Date(b.date).getTime() ||
          a.time.localeCompare(b.time)
        )
      }));
  }

  private getMonday(date: Date): Date {
    date = new Date(date);
    const day = date.getDay(); // 0 (domingo) a 6 (sábado)
    
    if (day === 1) {
      date.setHours(0, 0, 0, 0);
      return date;
    }
  
    const diff = date.getDate() - day + (day === 0 ? -6 : 1);
    date.setDate(diff);
    date.setHours(0, 0, 0, 0);
  
    return date;
  }
  
  // Helper para formatear fecha
  private formatDate(date: Date): string {
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }



  selectShift(shift: { date: string; time: string }): void {
    this.selectedShift = shift;
    this.appointmentForm.get('shift')?.setValue(shift.time);
  }

  async saveShift(): Promise<void> {
    const result = await this.openVerifyDialog('200ms', '200ms');
    if (this.selectedShift && result === true) {
      this.isShiftModalVisible = false;
    } else {
      this.closed.emit();
    }
  }

  clearShift(): void {
    this.isShiftModalVisible = false;
    this.selectedShift = undefined;
    this.closed.emit();
  }

  onSubmit(): void {
    const token = this._authService.getInfoToken();
    this._userService.getUserByUsername(token.sub).subscribe({
      next: (response) => {
        if (this.appointmentForm.valid && this.selectedShift) {
          const formValue = this.appointmentForm.getRawValue();
          const appointmentData = {
            patientDni: this.patientDni,
            professionalDni: formValue.professional,
            secretaryDni: response.dni,
            specialityName: formValue.speciality,
            date: this.selectedShift.date,
            time: this.selectedShift.time
          };
          this.appointmentCreated.emit(appointmentData);
        }
      }
    });
  }

  private openVerifyDialog(enterAnimationDuration: string, exitAnimationDuration: string): Promise<boolean> {
    const dialogRef = this._dialog.open(VerifyDialogComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
    return new Promise((resolve) => {
      dialogRef.afterClosed().subscribe((response) => {
        resolve(response);
      });
    });
  }
}


