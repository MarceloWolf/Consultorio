import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Patient } from 'src/app/core/models/patient.model';
import { PatientService } from 'src/app/core/services/patient.service';

@Component({
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.css']
})
export class AddPatientComponent {
  @Output() patientAdded = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  addForm = new FormGroup({
    dni: new FormControl('', [Validators.required, Validators.pattern('^[0-9]{7,8}$')]),
    name: new FormControl('', Validators.required),
    lastname: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    phoneNumber: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    birthdate: new FormControl('', Validators.required)
  });
  errorMessage: string | null = null;

  constructor(private _patientService: PatientService) {}

  onSubmit(): void {
    if (this.addForm.invalid) return;

    const patient: Patient = {
      dni: this.addForm.value.dni!,
      name: this.addForm.value.name!,
      lastname: this.addForm.value.lastname!,
      email: this.addForm.value.email!,
      phoneNumber: this.addForm.value.phoneNumber!,
      address: this.addForm.value.address!,
      birthdate: this.addForm.value.birthdate!,
      active:true
    };

    this._patientService.createPatient(patient).subscribe({
      next: () => {
        this.patientAdded.emit();
        this.close();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al crear el paciente:', err);
        if (err.error && typeof err.error === 'object') {
          this.errorMessage = Object.values(err.error).join(', ');
        } else {
          this.errorMessage = err.error?.message || 'Ocurrió un error';
        }
      }
    });
  }

  close(): void {
    this.closed.emit();
    this.addForm.reset();
    this.errorMessage = null;
  }

}
