import { MedicalAppointmentStateEnum } from "./medical-appointment.model";

export interface FullMedicalAppointment {
    medicalAppointmentId: number;
    specialityName: string;
    professionalDni: string;
    professionalName: string;
    professionalLastname: string;
    secretaryDni: string;
    patientDni: string;
    patientName: string;
    patientLastname: string;
    state:MedicalAppointmentStateEnum;
    date: string; 
    time: string;
}