import { ProfessionalSpeciality } from './professionalSpeciality.model';

export interface Speciality {
    specialityId?: number; 
    name: string;
    professionalList?: ProfessionalSpeciality[];
}