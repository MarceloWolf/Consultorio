import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Professional } from 'src/app/core/models/professional.model';
import { Speciality } from 'src/app/core/models/speciality.model';
import { PatientService } from 'src/app/core/services/patient.service';
import { ProfessionalService } from 'src/app/core/services/professional.service';
import { SpecialityService } from 'src/app/core/services/speciality.service';

@Component({
  selector: 'app-view-specialities-dialog',
  templateUrl: './view-specialities-dialog.component.html',
  styleUrls: ['./view-specialities-dialog.component.css']
})
export class ViewSpecialitiesDialogComponent implements OnInit {

  specialitiesList:Speciality[] = [];
  professionalsList:Professional[] = [];
  constructor(public dialogRef: MatDialogRef<ViewSpecialitiesDialogComponent>, private _specialityService:SpecialityService
    , private _patientService:PatientService
  ){
    
  }
  ngOnInit(): void {
    this._specialityService.getSpecialities().subscribe({
      next: (response) => {
        this.specialitiesList = response;
      },
      error: () => {
        this.specialitiesList = [];
      },
    }) 
  }

  send(speciality:string){
    this._patientService.getProfessionalsBySpeciality(speciality).subscribe({
      next: (response) => {
        if(response.length > 0)
        {
          alert("No se puede eliminar una especialidad que tenga uno o mas profesionales relacionados")
        }
        else
        {
          this._specialityService.deleteSpeciality(speciality).subscribe({
            next:() => {
              alert("Especialidad " + speciality + " eliminada correctamente");
            }
          })
        }
      }
    })
    this.dialogRef.close();
  }

  cancel(){
    this.dialogRef.close();
  }

}
