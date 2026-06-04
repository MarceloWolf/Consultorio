import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AccountStateEnum, User } from 'src/app/core/models/user.model';
import { BusinessDays } from '../../core/models/businessDays.model';
import { Speciality } from '../../core/models/speciality.model';
import { SpecialityService } from '../../core/services/speciality.service';

@Component({
  selector: 'app-add-professional-dialog',
  templateUrl: './add-professional-dialog.component.html',
  styleUrls: ['./add-professional-dialog.component.css']
})
export class AddProfessionalDialogComponent {
  @Output() formSubmit = new EventEmitter<FormGroup>();
  @Output() cancelForm = new EventEmitter<boolean>();
  userForm!: FormGroup;
  specialities:Speciality[] = [];
  businessDaysOptions = [
    { value: "MONDAY", label: "Lunes" },
    { value: "TUESDAY", label: "Martes" },
    { value: "WEDNESDAY", label: "Miércoles" },
    { value: "THURSDAY", label: "Jueves" },
    { value: "FRIDAY", label: "Viernes" },
    { value: "SATURDAY", label: "Sábado" },
  ];
  constructor(private specialityService:SpecialityService,private fb: FormBuilder,public dialogRef: MatDialogRef<AddProfessionalDialogComponent> ) {}

  ngOnInit() {
    this.cancelForm.emit(false);
    this.userForm = this.fb.group({
      dni: ['', Validators.required],
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      address: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      accountState: [AccountStateEnum.ACTIVE],
      newAccount: [false],
      start: ['', Validators.required],
      end: ['', Validators.required],
      businessDays: [[],[Validators.required, Validators.minLength(1)]],
      speciality: [[],[Validators.required]],     
    });

    this.specialityService.getSpecialities().subscribe((speciality:Speciality[]) => {
      this.specialities = speciality;
    })

    this.setupConditionalValidation();
  }

  setupConditionalValidation() {
    this.userForm.get('role')?.valueChanges.subscribe((role) => {
      if (role === 'PROFESSIONAL') {
        this.userForm.get('businessDays')?.setValidators([Validators.required]);
        this.userForm.get('speciality')?.setValidators([Validators.required]);
      } else {
        this.userForm.get('businessDays')?.clearValidators();
        this.userForm.get('speciality')?.clearValidators();
        this.userForm.get('businessDays')?.setValue([]);
        this.userForm.get('speciality')?.setValue([]);
      }
      this.userForm.get('businessDays')?.updateValueAndValidity();
      this.userForm.get('speciality')?.updateValueAndValidity();
    });
  }

  updateBusinessDays(event: Event, value: string) {
    const checked = (event.target as HTMLInputElement).checked;
    let selectedDays = this.userForm.get('businessDays')?.value || [];

    if (checked) {
      selectedDays.push(value);
    } else {
      selectedDays = selectedDays.filter((day: string) => day !== value);
    }

    this.userForm.get('businessDays')?.setValue(selectedDays);
  }

  updateSpecialities(event: Event, value: string) {
    const checked = (event.target as HTMLInputElement).checked;
    let selectedSpecialities = this.userForm.get('speciality')?.value || [];

    if (checked) {
      selectedSpecialities.push(value);
    } else {
      selectedSpecialities = selectedSpecialities.filter((speciality: string) => speciality !== value);
    }

    this.userForm.get('speciality')?.setValue(selectedSpecialities);
  }

  submitForm() {
    if (this.userForm!.valid) {
      this.formSubmit.emit(this.userForm);
    }
  }

  cancel() {
    this.cancelForm.emit(true);
    this.dialogRef.close();
  }
}

