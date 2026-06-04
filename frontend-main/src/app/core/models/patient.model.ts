import { MedicalRecord } from "./medical-record.model";

export interface Patient {
    dni: string; 
    name: string;
    lastname: string;
    address: string;
    email: string;
    phoneNumber: string;
    birthdate: string; 
    active: boolean;
    medicalRecord?: MedicalRecord;
}