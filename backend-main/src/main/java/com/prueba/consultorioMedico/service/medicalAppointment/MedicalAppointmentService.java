package com.prueba.consultorioMedico.service.medicalAppointment;

import com.prueba.consultorioMedico.dto.AppointmentFilterDto;
import com.prueba.consultorioMedico.dto.FullMedicalAppointmentDto;
import com.prueba.consultorioMedico.dto.MedicalAppointmentDataAllowedToUpdateDto;
import com.prueba.consultorioMedico.dto.SimpleMedicalAppointmentDto;
import com.prueba.consultorioMedico.enums.MedicalAppointmentStateEnum;
import com.prueba.consultorioMedico.exception.PatientAlreadyHasAppointmentException;
import com.prueba.consultorioMedico.exception.ProfessionalUnavailableException;
import com.prueba.consultorioMedico.model.*;
import com.prueba.consultorioMedico.repository.*;
import com.prueba.consultorioMedico.util.DateValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService implements IMedicalAppointmentService {
        private final IMedicalAppointmentRepository medicalAppointmentRepository;
        private final IProfessionalRepository professionalRepository;
        private final ISecretaryRepository secretaryRepository;
        private final IPatientRepository patientRepository;
        private final ISpecialityRepository specialityRepository;

        @Override
        @Transactional(readOnly = true)
        public SimpleMedicalAppointmentDto findById(Long medicalAppointmentId) {
                MedicalAppointment medicalAppointment = medicalAppointmentRepository.findById(medicalAppointmentId)
                                .orElseThrow();
                // Obtengo las entidades
                Professional professional = professionalRepository
                                .findById(medicalAppointment.getProfessional().getId())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + medicalAppointment.getPatientDni()
                                                                + " not found"));
                Speciality speciality = specialityRepository
                                .findById(medicalAppointment.getSpeciality().getSpecialityId())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Speciality " + medicalAppointment.getPatientDni()
                                                                + " not found"));
                Patient patient = patientRepository.findById(medicalAppointment.getPatient().getDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Patient " + medicalAppointment.getPatientDni()
                                                                + " not found"));
                // Preparo la respuesta y retorno
                return SimpleMedicalAppointmentDto.builder()
                                .professionalDni(professional.getDni())
                                .specialityName(speciality.getName())
                                .patientDni(patient.getDni())
                                .date(medicalAppointment.getAppointmentDate()).build();
        }

        @Override
        @Transactional(readOnly = true)
        public List<FullMedicalAppointmentDto> findAllByPatient(String patientDni) {
                Patient patient = patientRepository.findById(patientDni).orElseThrow(() -> new NoSuchElementException(
                                "Patient " + patientDni
                                                + " not found"));
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository
                                .findAllByPatient(patient);
                return this.formatData(medicalAppointmentList);
        }

        @Override
        public List<FullMedicalAppointmentDto> findAllByPatientAndSpecialty(String patientDni, String specialityName) {
                Patient patient = patientRepository.findById(patientDni).orElseThrow(() -> new NoSuchElementException(
                        "Patient " + patientDni + " not found"));
                Speciality speciality = specialityRepository.findByName(specialityName)
                        .orElseThrow(() -> new NoSuchElementException("Specialidad no encontrada"));
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository.findAllByPatientAndSpeciality(patient, speciality);
                return this.formatData(medicalAppointmentList);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FullMedicalAppointmentDto> findAllByProfessional(String professionalDni) {
                Professional professional = professionalRepository.findByDNI(professionalDni)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + professionalDni
                                                                + " not found"));
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository
                                .findAllMedicalAppointmentByProfessional(professional.getDni());
                return this.formatData(medicalAppointmentList);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FullMedicalAppointmentDto> findAllBySpeciality(String specialityName) {
                Speciality speciality = specialityRepository.findByName(specialityName)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + specialityName
                                                                + " not found"));
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository
                                .findAllBySpeciality(speciality);
                return this.formatData(medicalAppointmentList);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FullMedicalAppointmentDto> findAllByFilters(String specialityName, String professionalDni, LocalDate selectedDate) {
                
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository
                                .findByFilters(specialityName,professionalDni,selectedDate);
                return this.formatData(medicalAppointmentList);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FullMedicalAppointmentDto> findAll() {
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository.findAll();
                return this.formatData(medicalAppointmentList);
        }

        @Override
        @Transactional
        public void add(FullMedicalAppointmentDto medicalAppointmentDto) {
                Professional professional = professionalRepository.findByDNI(medicalAppointmentDto.getProfessionalDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + medicalAppointmentDto.getProfessionalDni()
                                                                + " not found"));

                Patient patient = patientRepository.findById(medicalAppointmentDto.getPatientDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Patient " + medicalAppointmentDto.getPatientDni()
                                                                + " not found"));
                Speciality speciality = specialityRepository.findByName(medicalAppointmentDto.getSpecialityName())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Speciality " + medicalAppointmentDto.getSpecialityName()
                                                                + " not found"));

                Secretary secretary = secretaryRepository.findByDni(medicalAppointmentDto.getSecretaryDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Secretary " + medicalAppointmentDto.getSecretaryDni()
                                                                + " not found"));

                if (validateAppointment(medicalAppointmentDto.getDate(),
                                medicalAppointmentDto.getTime(), professional.getDni(), patient.getDni())) {

                        professional.getBusinessDaysList().stream()
                        .filter(day -> medicalAppointmentDto.getDate().getDayOfWeek().equals(day.getDay()))
                        .forEach(day -> day.getShift().stream() //Con este stream puedo continuar el flujo en una lista de una lista
                                .filter(shift -> shift.getShiftTime().equals(medicalAppointmentDto.getTime()))
                                .forEach(shift -> shift.setShiftReserved(true)));
                        
                        professionalRepository.save(professional);

                        // Armo el objeto que se guardara en la bd
                        MedicalAppointment medicalAppointment = MedicalAppointment.builder()
                                        .appointmentDate(medicalAppointmentDto.getDate())
                                        .appointmentTime(medicalAppointmentDto.getTime())
                                        .secretary(secretary)
                                        .speciality(speciality)
                                        .patient(patient)
                                        .professional(professional)
                                        .state(MedicalAppointmentStateEnum.INICIADO)
                                        .build();
                        medicalAppointmentRepository.save(medicalAppointment);
                }
        }

        @Override
        @Transactional
        public void deleteAppointment(Long appointmentId) {
                MedicalAppointment medicalAppointment = medicalAppointmentRepository.findById(appointmentId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Medical Appointment " + appointmentId
                                                                + " not found"));
                // Verificar si el turno se puede eliminar en base a la fecha y hora
                DateValidation.validateAbleToModifyOrDelete(medicalAppointment.getAppointmentTime(),
                                medicalAppointment.getAppointmentDate());
                medicalAppointmentRepository.deleteById(appointmentId);
        }

        @Override
        @Transactional
        public void updateAppointment(Long id,MedicalAppointment medicalAppointment) {

                if (validateAppointment(medicalAppointment.getAppointmentDate(),
                                medicalAppointment.getAppointmentTime(), medicalAppointment.getProfessionalDni(),
                                medicalAppointment.getPatientDni())) {
                                        MedicalAppointment aux = medicalAppointmentRepository.findById(id)
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "Medical Appointment " + id
                                                                        + " not found"));

                        medicalAppointment.setMedicalAppointmentId(id);
                        aux.setAppointmentDate(medicalAppointment.getAppointmentDate());
                        aux.setAppointmentTime(medicalAppointment.getAppointmentTime());
                        aux.setState(medicalAppointment.getState());

                       medicalAppointment = medicalAppointmentRepository.save(aux);
                }
        }

        private boolean validateAppointment(LocalDate date, LocalTime time, String professionalDni, String patientDni) {
                validateExistenceOfAppointment(time, date, professionalDni, patientDni);
                // Verificar que el nuevo turno cumpla con las restricciones
                DateValidation.validateAppointmentTime(time);
                DateValidation.validateDayOfWeek(date);
                // Verificar que las entidades existan
                Professional professional = professionalRepository.findByDNI(professionalDni)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + professionalDni
                                                                + " not found"));// Arreglar la reprogramacion de un
                                                                                 // turno
                // Verificar que el profesional pueda atender el nuevo turno
                DateValidation.validateProfessionalTime(time,
                                professional.getStart(),
                                professional.getEnd());
                DateValidation.validateDateAndTime(date,
                                time);
                // Verificar si el turno se puede modificar
                DateValidation.validateAbleToModifyOrDelete(time,
                                date);
                DateValidation.validateDateAndTime(date, time, professional);

                return true;
        }

        @Override
        @Transactional
        public void updateAppointmentState(Long appointmentId, String newState) {
                MedicalAppointment medicalAppointment = medicalAppointmentRepository.findById(appointmentId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Medical Appointment " + appointmentId
                                                                + " not found"));

                medicalAppointment.setState(MedicalAppointmentStateEnum.valueOf(newState));
/*                 this.updateAppointment(medicalAppointment); */
        }

        @Override
        @Transactional
        public void cancel(Long medicalAppointmentId) {
                // Para comprobar que exista
                MedicalAppointment medicalAppointment = medicalAppointmentRepository.findById(medicalAppointmentId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Medical Appointment " + medicalAppointmentId
                                                                + " not found"));
                // Verifico si se puede eliminar
                DateValidation.validateAbleToModifyOrDelete(medicalAppointment.getAppointmentTime(),
                                medicalAppointment.getAppointmentDate());
                medicalAppointment.setState(MedicalAppointmentStateEnum.CANCELADO);
        }

        // Aux
        // Lo que hago en este metodo es formatear la informacion de la lista de turnos
        // La diferencia es que en la lista de turnos normal tiene objetos como
        // profesionales, pacientes, especialidades
        // Para hacerlo mas simple itero cada turno, desarmo sus objetos y lo guardo en
        // la lista de dtos.
        // Al terminar retorno la lista con todos los dto
        public List<FullMedicalAppointmentDto> formatData(List<MedicalAppointment> medicalAppointmentList) {
                List<FullMedicalAppointmentDto> medicalAppointmentDtoList = new ArrayList<>();

                medicalAppointmentList.forEach((medicalAppointment -> {
                        FullMedicalAppointmentDto dto = FullMedicalAppointmentDto.builder()
                                        .medicalAppointmentId(medicalAppointment.getMedicalAppointmentId())
                                        .professionalDni(medicalAppointment.getProfessional().getDni())
                                        .professionalName(medicalAppointment.getProfessional().getName())
                                        .professionalLastname(medicalAppointment.getProfessional().getLastname())
                                        .secretaryDni(medicalAppointment.getSecretary().getDni())
                                        .patientDni(medicalAppointment.getPatient().getDni())
                                        .patientName(medicalAppointment.getPatient().getName())
                                        .patientLastname(medicalAppointment.getPatient().getLastname())
                                        .specialityName(medicalAppointment.getSpeciality().getName())
                                        .state(medicalAppointment.getState())
                                        .date(medicalAppointment.getAppointmentDate())
                                        .time(medicalAppointment.getAppointmentTime())
                                        .build();
                        medicalAppointmentDtoList.add(dto);
                }));
                return medicalAppointmentDtoList;
        }

        // Aux
        // Hago este metodo porque le metodo generico me pide un fullDto
        // Para el usuario seria tedioso llenar toda esa informacion
        // Lo que hago en este metodo es construir el dto full y llamar al otro metodo
        // add
        @Override
        @Transactional
        public void add(SimpleMedicalAppointmentDto simpleMedicalAppointmentDto) {
                Professional professional = professionalRepository
                                .findByDNI(simpleMedicalAppointmentDto.getProfessionalDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Professional " + simpleMedicalAppointmentDto.getProfessionalDni()
                                                                + " not found"));

                Patient patient = patientRepository.findById(simpleMedicalAppointmentDto.getPatientDni())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Patient " + simpleMedicalAppointmentDto.getPatientDni()
                                                                + " not found"));

                Speciality speciality = specialityRepository.findByName(simpleMedicalAppointmentDto.getSpecialityName())
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Speciality " + simpleMedicalAppointmentDto.getSpecialityName()
                                                                + " not found"));

                FullMedicalAppointmentDto fullMedicalAppointmentDto = FullMedicalAppointmentDto.builder()
                                .professionalDni(professional.getDni()).professionalName(professional.getName())
                                .professionalLastname(professional.getLastname())
                                .patientDni(patient.getDni()).patientName(patient.getName())
                                .patientLastname(patient.getLastname())
                                .specialityName(speciality.getName())
                                .secretaryDni(simpleMedicalAppointmentDto.getSecretaryDni())
                                .date(simpleMedicalAppointmentDto.getDate())
                                .time(simpleMedicalAppointmentDto.getTime())
                                .build();
                // Una vez ya formateado llamo al otro metodo add de este servicio, donde ahi se
                // guardara la cita
                this.add(fullMedicalAppointmentDto);
        }

        // Generar validacion de que no se puede reprogramar un turno a una fecha y hora
        // ya ocupada.
        @Override
        @Transactional
        public void reschedule(Long id, MedicalAppointmentDataAllowedToUpdateDto mAllowedToUpdateDto) {

                MedicalAppointment mAppointment = medicalAppointmentRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException(
                                                "Medical Appointment " + id
                                                                + " not found"));

                        MedicalAppointment medicalAppointment = new MedicalAppointment();
                        
                        medicalAppointment.setAppointmentTime(mAllowedToUpdateDto.getAppointmentTime());
                        medicalAppointment.setAppointmentDate(mAllowedToUpdateDto.getAppointmentDate());
                        medicalAppointment.setPatient(mAppointment.getPatient());
                        medicalAppointment.setProfessional(mAppointment.getProfessional());
                        medicalAppointment.setState(MedicalAppointmentStateEnum.REPROGRAMADO);
                      
                this.updateAppointment(id,medicalAppointment);
        }

        @Override
        public List<FullMedicalAppointmentDto> getMedicalAppointmentsByState(MedicalAppointmentStateEnum appointmentStateEnum) {
                 List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository.findAllByState(appointmentStateEnum);
                return formatData(medicalAppointmentList);
        }

        @Override
        public List<FullMedicalAppointmentDto> findAllByDate(LocalDate date) {
                List<MedicalAppointment> medicalAppointmentList = medicalAppointmentRepository.findAllByAppointmentDate(date);
                return formatData(medicalAppointmentList);
        }

        // region validaciones turnos

        private void validateExistenceOfAppointment(LocalTime appointmentTime, LocalDate appointmentDate,
                        String profDni, String patientDni) {
                                
                                Professional professional = professionalRepository
                                .findByDNI(profDni)
                                .orElseThrow(() -> new NoSuchElementException(
                                        "Professional " + profDni
                                        + " not found"));

                if (medicalAppointmentRepository.findAppointmentByProfessional(appointmentDate, appointmentTime,professional.getId()).isPresent()) {
                        throw new ProfessionalUnavailableException(
                                        "El profesional ya tiene un turno para ese dia y fecha");
                }
                if (medicalAppointmentRepository.findAppointmentByPatient(appointmentDate, appointmentTime, patientDni)
                                .isPresent()) {
                        throw new PatientAlreadyHasAppointmentException(
                                        "Usted ya cuenta con un turno para ese dia y fecha");
                }
        }

        // endregion

}
