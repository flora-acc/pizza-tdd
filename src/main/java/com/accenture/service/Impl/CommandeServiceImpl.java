package com.accenture.service.Impl;

import com.accenture.exception.CommandeException;
import com.accenture.repository.PizzaDao;
import com.accenture.service.Interface.CommandeService;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CommandeServiceImpl implements CommandeService {


    public static final String ERREUR_VERIFICATION_COMMANDE = "Erreur verification Commande : {}";
private PizzaDao pizzaDao;

    /**
     * Ajoute une nouvelle commande dans la base données
     * @param commandeRequestDto Objet contenant les informations de la commande à ajouter
     * @return un objet CommandeResponseDto qui représente la commande à ajouter
     */
    @Override
    public CommandeResponseDto ajouter(CommandeRequestDto commandeRequestDto) throws CommandeException {
        verificationCommande(commandeRequestDto);
        return null;
    }

    //    *************************************************************************
    //    ************************ METHODES PRIVEES *******************************
    //    *************************************************************************

    private static void verificationCommande(CommandeRequestDto commandeRequest) {
        String message = "";
        if (commandeRequest == null) {
            message = "La commande est null";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }
        if (commandeRequest.idClient() == null) {
            message = "Le client est obligatoire";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }

        if (commandeRequest.commande() == null) {
            message = "Le contenu de la commande est obligatoire";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }
        verifierCommandeContenu(commandeRequest);
    }

    private static void verifierCommandeContenu(CommandeRequestDto commandeRequest) {
        String message;
        for (int i = 0; i < commandeRequest.commande().size(); i++) {
            if (commandeRequest.commande().get(i).idPizza() == null) {
                message = "L'Id de la Pizza est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }
            if (commandeRequest.commande().get(i).taille() == null) {
                message = "La taille est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }
            if (commandeRequest.commande().get(i).quantite() == null) {
                message = "La quantite est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }

        }
    }

//private Commande toCommande (CommandeRequestDto commandeRequestDto){
//       pizzaDao.findAllById(commandeRequestDto.)
//        return null;
//}
//
//private  CommandeResponseDto toCommandeResponse (Commande commande){
//        return null;
//}



}
