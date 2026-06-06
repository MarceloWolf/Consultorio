import { Component, OnInit } from '@angular/core';
import {
  AccountStateEnum,
  RoleEnum,
  User,
} from '../../../core/models/user.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { AddProfessionalDialogComponent } from 'src/app/shared/add-professional-dialog/add-professional-dialog.component';
import { Professional } from '../../../core/models/professional.model';
import { Secretary } from 'src/app/core/models/secretary.model';
import { AddSpecialityDialogComponent } from '../../pop-up/add-speciality-dialog/add-speciality-dialog.component';
import { SpecialityService } from 'src/app/core/services/speciality.service';
import { VerifyDialogComponent } from 'src/app/shared/verify-dialog/verify-dialog.component';
import { Router } from '@angular/router';
import { ViewSpecialitiesDialogComponent } from '../../pop-up/view-specialities-dialog/view-specialities-dialog.component';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.css'],
})
export class AdminViewComponent implements OnInit {
  allUsers: User[] = [];
  filteredUsers: User[] = [];
  searchQuery: string = '';
  submitted: boolean = false;
  selectedUser: string | null = null;
  selectedType: string = '';
  selectedState: string = '';
  isEditing: boolean[] = [];
  originalUser: { [key: number]: User } = {};
  errors: { [key: number]: { [key: string]: string } } = {};
  userForm!: FormGroup;
  cancelResponse:boolean = false;
  dialogProfessionalRef!: MatDialogRef<AddProfessionalDialogComponent, any>;

  constructor(private specialityService: SpecialityService,
    private userService: UserService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.userService.getUsers().subscribe((data: User[]) => {
      this.allUsers = data;
      this.filterUsers();
      this.isEditing = new Array(this.allUsers.length).fill(false);
    });
    this.selectedUser = null;
    this.selectedType = '';
  }

  openDialog(
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
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
    dialogRef.afterClosed().subscribe((data) => {
      if (data === undefined) {
        this.updatePassword(username, data, false);
      }
      else {
        const dialogRef = this.dialog.open(VerifyDialogComponent, {
          width: '400px',
          enterAnimationDuration,
          exitAnimationDuration,
          hasBackdrop: true,
          disableClose: false,
          panelClass: 'custom-dialog-panel',
          backdropClass: 'cdk-overlay-dark-backdrop',
        });
        dialogRef.afterClosed().subscribe((response) => {
          this.updatePassword(username, data, response);
        })
      }
    })
  }

  openVerifyDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): Promise<boolean> {
    const dialogRef = this.dialog.open(VerifyDialogComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
    return new Promise((resolve, reject) => {
      dialogRef.afterClosed().subscribe((response) => {
        resolve(response);
      });
    });
  }


  openDialogAddSpeciality(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    const dialogRef = this.dialog.open(AddSpecialityDialogComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
    dialogRef.afterClosed().subscribe((data: string) => {
      if (data !== undefined) {
        const dialogRef = this.dialog.open(VerifyDialogComponent, {
          width: '400px',
          enterAnimationDuration,
          exitAnimationDuration,
          hasBackdrop: true,
          disableClose: false,
          panelClass: 'custom-dialog-panel',
          backdropClass: 'cdk-overlay-dark-backdrop',
        });
        dialogRef.afterClosed().subscribe((response) => {
          this.addSpeciality(data, response);
        })
      }
      else {
        return;
      }
    })

  }

  openDialogViewSpecialities(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.dialog.open(ViewSpecialitiesDialogComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });
  }

  openDialogAddProfessional(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.dialogProfessionalRef = this.dialog.open(AddProfessionalDialogComponent, {
      width: 'auto',
      enterAnimationDuration,
      exitAnimationDuration,
      hasBackdrop: true,
      disableClose: false,
      panelClass: 'custom-dialog-panel',
      backdropClass: 'cdk-overlay-dark-backdrop',
    });

    this.dialogProfessionalRef.componentInstance.cancelForm.subscribe((cancel:boolean) => {
       this.cancelResponse = cancel;
    })

    this.dialogProfessionalRef.componentInstance.formSubmit.subscribe((data: FormGroup) => {
      if (data.get('dni')?.value !== '' && this.cancelResponse === false) {
        const dialogRef = this.dialog.open(VerifyDialogComponent, {
          width: '400px',
          enterAnimationDuration,
          exitAnimationDuration,
          hasBackdrop: true,
          disableClose: false,
          panelClass: 'custom-dialog-panel',
          backdropClass: 'cdk-overlay-dark-backdrop',
        });
        dialogRef.afterClosed().subscribe((response) => {

          if (data.get('role')?.value === 'PROFESSIONAL') {
            this.professionalCreate(data, response);
          }
          else {
            this.secretaryCreate(data, response, '200ms', '200ms');
          }

        })
      }
      else {
        return;
      }
    })
  }

  onSearch(event: any) {
    this.searchQuery = event.target.value;
    this.filterUsers();
  }

  filterUsers() {
    let temp = [...this.allUsers];

    // Search query filter (DNI, Name, Lastname, username, email)
    if (this.searchQuery && this.searchQuery.trim() !== '') {
      const q = this.searchQuery.toLowerCase().trim();
      temp = temp.filter(u => 
        (u.dni && u.dni.toLowerCase().includes(q)) ||
        (u.name && u.name.toLowerCase().includes(q)) ||
        (u.lastname && u.lastname.toLowerCase().includes(q)) ||
        (u.username && u.username.toLowerCase().includes(q)) ||
        (u.email && u.email.toLowerCase().includes(q))
      );
    }

    // Role type filter
    if (this.selectedType && this.selectedType !== 'TODOS') {
      temp = temp.filter(u => u.role === this.selectedType);
    }

    // Account state filter
    if (this.selectedState && this.selectedState !== '' && this.selectedType && this.selectedType !== 'TODOS' && this.selectedType !== 'ADMIN') {
      temp = temp.filter(u => u.accountState === this.selectedState);
    }

    this.filteredUsers = temp;
  }

  toggleEdit(index: number, user: User) {
    this.originalUser[index] = { ...user };
    this.isEditing[index] = true;
  }

  async toggleSave(index: number, user: User) {
    const result = await this.openVerifyDialog('200ms', '200ms');
    if (result === true) {
      if (user.role === 'PROFESSIONAL') {
        this.userService.updateProfessional(user.dni, user).subscribe({
          next: () => {
            alert('Profesional actualizado con exito');
            this.ngOnInit();
          },
          error: (err) => {
            if (err.status === 400 && err.error) {
              this.errors[index] = err.error;
              this.toggleCancel(index);
            } else {
              alert(err.error?.message || 'Error desconocido');
              this.toggleCancel(index);
            }
          },
        });
      } else if (user.role === 'SECRETARY') {
        this.userService.updateSecretary(user.dni, user).subscribe({
          next: () => {
            alert('Secretaria/o actualizado con exito');
            this.ngOnInit();
          },
          error: (err) => {
            if (err.status === 400 && err.error) {
              this.errors[index] = err.error;
              const errorMessage = Object.values(err.error).join('\n');
              alert(`Errores de validación:\n${errorMessage}`);
              this.toggleCancel(index);
            } else {
              alert(err.error?.message || 'Error desconocido');
              this.toggleCancel(index);
            }
          },
        });
      }
    }
    else {
      this.toggleCancel(index);
    }
    this.isEditing[index] = false;
  }

  toggleCancel(index: number) {
    if (this.originalUser[index]) {
      this.filteredUsers[index] = { ...this.originalUser[index] };
      delete this.originalUser[index];
    }
    this.isEditing[index] = false;
  }

  updatePassword(username: string, newPassword: string, response: boolean) {
    if (newPassword === undefined || response === false) {
      alert("Solicitud cancelada");
      return;
    }
    this.userService.updatePassword(username, newPassword).subscribe({
      next: () => {
        alert('Contraseña de usuario ' + username + ' actualizada con exito');
        this.ngOnInit();
      },
      error: (err) => {
        console.log(newPassword);
        const errorMessage = err.error?.message || 'Error desconocido';
        alert(errorMessage);
      },
    });
  }

  addSpeciality(newSpeciality: string, response: boolean) {
    if (newSpeciality === undefined || response === false) {
      return;
    }
    this.specialityService.addSpeciality(newSpeciality).subscribe({
      next: () => {
        alert('Especialidad agregada con exito');
      },
      error: (err) => {
        const errorMessage = err.error?.message || 'Error desconocido';
        alert(errorMessage);
      },
    });
  }

  async deleteUser(dni: string, role: string) {
    const result = await this.openVerifyDialog('200ms', '200ms');
    if (result !== false) {
      if (role === 'PROFESSIONAL') {
        this.userService.deleteProfessional(dni).subscribe({
          next: () => {
            alert('Usuario dado de baja con exito');
            this.ngOnInit();
          },
          error: (err) => {
            const errorMessage = err.error?.message || 'Error desconocido';
            alert(errorMessage);
          },
        });
      } else if (role === 'SECRETARY') {
        this.userService.deleteSecretary(dni).subscribe({
          next: () => {
            alert('Usuario dado de baja con exito');
            this.ngOnInit();
          },
          error: (err) => {
            const errorMessage = err.error?.message || 'Error desconocido';
            alert(errorMessage);
          },
        });
      }
    }
  }

  reactivateUser(dni: string) {
    this.userService.reactivateUser(dni).subscribe({
      next: () => {
        alert('Usuario dado de alta con exito');
        this.ngOnInit();
      },
      error: (err) => {
        const errorMessage = err.error?.message || 'Error desconocido';
        alert(errorMessage);
      },
    });
  }

  professionalCreate(data: FormGroup, response: boolean) {
    const professional: Professional = {
      dni: data.get('dni')?.value,
      name: data.get('name')?.value,
      lastname: data.get('lastname')?.value,
      address: data.get('address')?.value,
      email: data.get('email')?.value,
      phoneNumber: data.get('phoneNumber')?.value,
      username: data.get('username')?.value,
      password: data.get('password')?.value,
      role: data.get('role')?.value,
      accountState: data.get('accountState')?.value,
      newAccount: data.get('newAccount')?.value,
      start: data.get('start')?.value,
      end: data.get('end')?.value,
      businessDays: data.get('businessDays')?.value || [],
      specialityList: data.get('speciality')?.value || []
    };
    if (response === false) {
      return;
    }
    this.userService.createProfessional(professional).subscribe({
      next: () => {
        alert('Profesional creado con exito');
        this.dialogProfessionalRef.close();
        this.ngOnInit();
      },
      error: (err) => {
        if (err.status === 400 && err.error) {
          const errorMessage = Object.values(err.error).join('\n');
          alert(`Errores de validación:\n${errorMessage}`);
        } else {
          alert(err.error?.message || 'Error desconocido dentro de profCreate');
        }
      },
    });
  }
  secretaryCreate(data: FormGroup, response: boolean, enterAnimationDuration: string, exitAnimationDuration: string) {
    const secretary: Secretary = {
      dni: data.get('dni')?.value,
      name: data.get('name')?.value,
      lastname: data.get('lastname')?.value,
      address: data.get('address')?.value,
      email: data.get('email')?.value,
      phoneNumber: data.get('phoneNumber')?.value,
      username: data.get('username')?.value,
      password: data.get('password')?.value,
      role: data.get('role')?.value,
      accountState: data.get('accountState')?.value,
      newAccount: data.get('newAccount')?.value,
      start: data.get('start')?.value,
      end: data.get('end')?.value,
    };
    if (response === false) {
      return;
    }
    this.userService.createSecretary(secretary).subscribe({
      next: () => {
        alert('Secretaria/o creado con exito');
        this.dialogProfessionalRef.close();
        this.ngOnInit();
      },
      error: (err) => {
        if (err.status === 400 && err.error) {
          const errorMessage = Object.values(err.error).join('\n');
          alert(`Errores de validación:\n${errorMessage}`);
        } else {
          alert(err.error?.message || 'Error desconocido');
        }
      },
    });
  }

  navigateToSecretary() {
    this.router.navigate(['/secretary']);
  }
  navigateToProfessional() {
    this.router.navigate(['/professional']);
  }


}
