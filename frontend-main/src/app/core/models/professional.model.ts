import { User } from './user.model';
import { ProfessionalSpeciality } from './professionalSpeciality.model';
import { Consultation } from './consultation.model';
import { BusinessDays } from './businessDays.model';

export interface Professional extends User {
    specialityList?: ProfessionalSpeciality[];
    consultationList?: Consultation[];
    start: string; 
    end: string; 
    businessDays?: BusinessDays[];
}