import { Speciality } from './speciality.model';
import { Patient } from './patient.model';
import { Professional } from './professional.model';
import { Secretary } from './secretary.model';

export interface MedicalAppointment {
    medicalAppointmentId?: number; 
    speciality: Speciality;
    patient: Patient;
    professional: Professional;
    secretary: Secretary;
    appointmentDate: string; 
    appointmentTime: string; 
    state: MedicalAppointmentStateEnum;
}

export enum MedicalAppointmentStateEnum {
    INICIADO = "INICIADO",
    CANCELADO = "CANCELADO",
    REPROGRAMADO = "REPROGRAMADO"
}