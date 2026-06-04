import { MedicalRecord } from './medical-record.model';

export interface Consultation {
    id?: number;
    medicalRecord: MedicalRecord;
    professionalId: number;
    specialityName: string;
    date: string; 
    time: string; 
    reason: string;
    diagnosis: string;
    treatment: string;
}