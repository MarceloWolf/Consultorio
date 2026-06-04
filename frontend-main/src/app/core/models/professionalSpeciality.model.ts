import { Professional } from './professional.model';
import { Speciality } from './speciality.model';

export interface ProfessionalSpeciality {
    id?: number; 
    professional: Professional;
    speciality: Speciality;
}