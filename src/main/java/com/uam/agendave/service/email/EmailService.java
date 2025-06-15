package com.uam.agendave.service.email;

import com.uam.agendave.dto.MeetingDetailsDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMeetingInvitation(String to, String subject, MeetingDetailsDTO details) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlBody = buildEmailBody(details);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the error properly
            e.printStackTrace(); // Reemplaza con un logger real en producción
        }
    }

    private String buildEmailBody(MeetingDetailsDTO details) {
        return """
                        <html>
                                       <body style="font-family: Arial, sans-serif; color: #333;">
                                         <h2 style="color: #004080;">Confirmación de inscripción a actividad %s</h2>
                
                                         <p><strong>Propósito:</strong> Informarte que has sido inscrito correctamente en la actividad programada y brindarte los detalles necesarios para asegurar tu participación.</p>
                
                                         <p><strong>Dirigido a:</strong> Estudiantes inscritos en la actividad.</p>
                
                                         <p><strong>Detalles de la actividad:</strong><br>
                                            <strong>Fecha:</strong> %s<br>
                                            <strong>Hora:</strong> %s<br>
                                            <strong>Lugar:</strong> %s
                                         </p>
                
                
                                         <p><strong>Confirmación:</strong> No es necesario confirmar. Tu inscripción ya ha sido registrada correctamente.</p>
                
                                         <p><strong>Recomendación:</strong> Se recomienda llegar con al menos 10 minutos de anticipación para evitar contratiempos.</p>
                
                                         <p>Ante cualquier duda, podés responder a este correo o comunicarte con el equipo organizador.</p>
                
                                         <p>Saludos cordiales,<br>%s</p>
                                       </body>
                                     </html>
                """.formatted(
                        details.getTitle(),
                details.getDate(),
                details.getTime(),
                details.getLocation(),
                details.getSenderName()
        );
    }
}
