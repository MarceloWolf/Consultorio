import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Consultation } from 'src/app/core/models/consultation.model';
import { CreateConsultation } from 'src/app/core/models/createConsultation.model';
import { ConsultationService } from 'src/app/core/services/consultation.service';
import { ProfessionalService } from 'src/app/core/services/professional.service';

@Component({
  selector: 'app-add-consultation',
  templateUrl: './add-consultation.component.html',
  styleUrls: ['./add-consultation.component.css']
})
export class AddConsultationComponent {
  addForm = new FormGroup({
    diagnosis: new FormControl('', Validators.required),
    treatment: new FormControl('', Validators.required),
    reason: new FormControl('', Validators.required)
  });

  errorMessage: string | null = null;
  isLoading = false;

  constructor(
    private _professionalService: ProfessionalService,
    public dialogRef: MatDialogRef<AddConsultationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      professionalDni: string,
      patientDni: string,
      specialityName: string,
      appointmentDate:string,
      appointmentTime:string
    }
  ) {}

  onSubmit(): void {
    const consultationData: CreateConsultation = {
      date: this.data.appointmentDate,
      time: this.data.appointmentTime,
      diagnosis: this.addForm.value.diagnosis!,
      treatment: this.addForm.value.treatment!,
      reason: this.addForm.value.reason!,
    };

    console.log(consultationData);
    this._professionalService.addConsultation(
      this.data.professionalDni,
      this.data.patientDni,
      this.data.specialityName,
      consultationData).subscribe({
      next: () => {
        this.dialogRef.close(true);
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Error al crear la consulta';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }


}
