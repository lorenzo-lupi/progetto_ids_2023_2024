package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SMTPRequestHandler;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.utils.CredentialsUtils;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;

public class MunicipalityAdminGenerationHandler {

    /**
     * Generates a municipality admin for the given municipality, setting its email to the given email
     * and adding the admin role to the user.
     * Username is set to the name of the municipality concatenated with "Admin",
     * while the password is randomly generated.
     * If the generation of the user is successful, an email containing the credentials is sent to the given
     * email address.
     *
     * @param municipalityID the id of the municipality
     * @param email the email of the admin
     * @return the id of the generated admin
     * @throws IllegalArgumentException if the given municipality id does not exist or if the email is not valid
     */
    public static long generateMunicipalityAdmin(long municipalityID, String email) {
        Municipality municipality = MunicipalityRepository.getInstance().getItemByID(municipalityID);
        String password = CredentialsUtils.getRandomPassword();
        User municipalityAdmin = new User(municipality.getName() + "Admin", email, password);
        municipalityAdmin.addRole(new Role(municipality, AuthorizationEnum.ADMINISTRATOR));

        //Google Workspace account needed
        //SMTPRequestHandler.sendEmail(email, "Credenziali di accesso",
        //   "Username: " + municipalityAdmin.getUsername() + "\nPassword: " + password);

        UserRepository.getInstance().add(municipalityAdmin);
        return municipalityAdmin.getID();
    }
}
