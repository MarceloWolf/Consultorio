import { Component, OnInit } from '@angular/core';
import { Patient } from 'src/app/core/models/patient.model';
import { FullMedicalAppointment } from 'src/app/core/models/full-medical-appointment.model';
import { MedicalRecord } from 'src/app/core/models/medical-record.model';
import { PatientService } from 'src/app/core/services/patient.service';
import { MedicalImageService, MedicalImage } from 'src/app/core/services/medical-image.service';
import { ToothStateService, ToothState } from 'src/app/core/services/tooth-state.service';

@Component({
  selector: 'app-patient-view',
  templateUrl: './patient-view.component.html',
  styleUrls: ['./patient-view.component.css']
})
export class PatientViewComponent implements OnInit {
  activeTab = 'appointments';
  
  // Search inputs
  dniQuery: string = '';
  nameQuery: string = '';
  
  allPatients: Patient[] = [];
  matchingPatients: Patient[] = [];
  selectedPatient: Patient | null = null;
  selectedRecord: MedicalRecord | null = null;
  selectedAppointments: FullMedicalAppointment[] = [];
  selectedImages: MedicalImage[] = [];
  selectedToothStates: ToothState[] = [];
  
  // General Clinic Analytics (Mock stats calculated or predefined for display)
  totalPatientsCount = 0;
  activePatientsCount = 0;
  inactivePatientsCount = 0;
  
  constructor(
    private _patientService: PatientService,
    private _medicalImageService: MedicalImageService,
    private _toothStateService: ToothStateService
  ) {}

  ngOnInit(): void {
    this.loadAllPatients();
  }

  loadAllPatients() {
    this._patientService.getPatients().subscribe({
      next: (data) => {
        this.allPatients = data;
        this.totalPatientsCount = data.length;
        this.activePatientsCount = data.filter(p => p.active).length;
        this.inactivePatientsCount = data.filter(p => !p.active).length;
      },
      error: (err) => console.error('Error loading patients for portal search', err)
    });
  }

  onSearchChange() {
    if (!this.dniQuery && !this.nameQuery) {
      this.matchingPatients = [];
      return;
    }
    
    this.matchingPatients = this.allPatients.filter(p => {
      const matchDni = !this.dniQuery || p.dni.includes(this.dniQuery);
      const fullName = `${p.name} ${p.lastname}`.toLowerCase();
      const matchName = !this.nameQuery || fullName.includes(this.nameQuery.toLowerCase());
      return matchDni && matchName;
    });
  }

  selectPatient(patient: Patient) {
    this.selectedPatient = patient;
    this.dniQuery = '';
    this.nameQuery = '';
    this.matchingPatients = [];
    this.activeTab = 'appointments';
    
    // Fetch patient appointments
    this._patientService.getMedicalAppointmentsByPatient(patient.dni).subscribe({
      next: (apps) => this.selectedAppointments = apps,
      error: (err) => console.error('Error loading appointments for patient portal', err)
    });

    // Fetch patient medical record
    this._patientService.getMedicalRecord(patient.dni).subscribe({
      next: (rec) => this.selectedRecord = rec,
      error: (err) => {
        console.error('Error loading medical record for patient portal', err);
        this.selectedRecord = null;
      }
    });

    // Fetch patient images
    this._medicalImageService.getMedicalImages(patient.dni).subscribe({
      next: (imgs) => this.selectedImages = imgs,
      error: (err) => console.error('Error loading images for patient portal', err)
    });

    // Fetch tooth states
    this._toothStateService.getToothStates(patient.dni).subscribe({
      next: (teeth) => this.selectedToothStates = teeth,
      error: (err) => console.error('Error loading tooth states for patient portal', err)
    });
  }

  deselectPatient() {
    this.selectedPatient = null;
    this.selectedRecord = null;
    this.selectedAppointments = [];
    this.selectedImages = [];
    this.selectedToothStates = [];
  }

  selectTab(tab: string) {
    this.activeTab = tab;
  }
}
