import { Component, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatOption } from '@angular/material/core';
import { MatDialogRef } from '@angular/material/dialog';
import { map, Observable, startWith } from 'rxjs';
import { Speciality } from '../../../core/models/speciality.model';
import { SpecialityService } from '../../../core/services/speciality.service';


@Component({
  selector: 'app-add-speciality-dialog',
  templateUrl: './add-speciality-dialog.component.html',
  styleUrls: ['./add-speciality-dialog.component.css']
})
export class AddSpecialityDialogComponent implements OnInit{
  @ViewChildren(MatOption) options!: QueryList<MatOption>;

  selected:boolean = false;
  availableSpecialities:string[] = [];
  currentSpecialities:Speciality[] = [];
  especialidadCtrl = new FormControl('');
  filteredSpecialities!:Observable<string[]>;
  constructor(public dialogRef: MatDialogRef<AddSpecialityDialogComponent>, private _specialityService:SpecialityService) {}
  ngOnInit(): void {
    this.availableSpecialities = [
      'Cardiologia', 'Pediatria', 'Dermatologia', 'Neurologia', 'Oftalmologia',
      'Ginecologia', 'Pediatria', 'MedicinaGeneral', 'CirugiaGeneral', 'Traumatologia',
      'Oncologia', 'Neurocirugia', 'Endocrinologia', 'Urologia', 'Reumatologia',
      'Psiquiatria', 'Neumologia', 'Gastroenterologia', 'Nefrologia', 'Hematologia',
      'Inmunologia', 'Odontologia', 'Fisioterapia', 'Otorrinolaringologia', 'Estetica',
      'MedicinaInterna', 'CirugiaEstetica', 'CirugiaPlastica', 'Maternidad', 'PediatriaNeonatal'
    ];
    
    this._specialityService.getSpecialities().subscribe({
      next: (response) => {
        this.currentSpecialities = response;
        this.availableSpecialities = this.availableSpecialities.filter(speciality => !this.currentSpecialities.some(data => data.name === speciality));
        this.filteredSpecialities = this.especialidadCtrl.valueChanges.pipe(
         startWith(''),
         map(data => this._filter(data || ''))
        );
      },
      error: () => {
        this.currentSpecialities = [];
      },
    })

  }
  
  checkSelected() {
    if(this.options.changes)
      {
      this.selected = true;
    }
  }
  
  private _filter(data:string):string[] //Se utiliza el _ para marcar que algo es privado, tambien lo puedo usar para los campos
  {
    const filterData = data.toLowerCase();
    return this.availableSpecialities.filter(speciality => speciality.toLowerCase().includes(filterData));
  }


  send(){
    if(this.especialidadCtrl.value === '')
    {
      alert("Debe ingresar una especialidad");
    }
    else
    {
      this.dialogRef.close(this.especialidadCtrl.value);
    }
  }

  cancel(){
    this.dialogRef.close();
  }

}
