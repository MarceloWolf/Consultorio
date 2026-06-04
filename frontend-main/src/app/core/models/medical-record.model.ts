import { Patient } from "./patient.model";

export interface MedicalRecord {
    id?: number; 
    description?: string;
    patient: Patient;
    date: string; 
    time: string;
    height?: number; 
    weight?: number;
    bloodGroup: string;
}