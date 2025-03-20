package com.accenture.service.Impl;

import com.accenture.exception.CommandeException;
import com.accenture.repository.ClientDao;
import com.accenture.repository.CommandeDao;
import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Commande;
import com.accenture.repository.model.PizzaTailleQte;
import com.accenture.service.Interface.CommandeService;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.service.dto.PizzaTailleQteRequestDto;
import com.accenture.service.dto.PizzaTailleQteResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CommandeServiceImpl implements CommandeService {


    public static final String ERREUR_VERIFICATION_COMMANDE = "Erreur verification Commande : {}";
    private PizzaDao pizzaDao;
    private ClientDao clientDao;
    private CommandeDao commandeDao;

    /**
     * Ajoute une nouvelle pizzaTailleQteList dans la base données
     *
     * @param commandeRequestDto Objet contenant les informations de la pizzaTailleQteList à ajouter
     * @return un objet CommandeResponseDto qui représente la pizzaTailleQteList à ajouter
     */
    //TODO Gérer Transactionnal


    @Transactional
    @Override
    public CommandeResponseDto ajouter(CommandeRequestDto commandeRequestDto) throws CommandeException {
        verificationCommande(commandeRequestDto);
       Commande commande = toCommande(commandeRequestDto);
       //retirerIngredientStock(commande);
       commandeDao.save(commande);
        return toCommandeResponse(commande);
    }

    //    *************************************************************************
    //    ************************ METHODES PRIVEES *******************************
    //    *************************************************************************






    private void verificationCommande(CommandeRequestDto commandeRequest) {
        String message = "";
        if (commandeRequest == null) {
            message = "La pizzaTailleQteList est null";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }
        if (commandeRequest.idClient() == null) {
            message = "Le client est obligatoire";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }

        if (commandeRequest.pizzaTailleQteList() == null) {
            message = "Le contenu de la pizzaTailleQteList est obligatoire";
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new CommandeException(message);
        }
        verifierCommandeContenu(commandeRequest);
    }

    private void verifierCommandeContenu(CommandeRequestDto commandeRequest) {
        String message;
        for (int i = 0; i < commandeRequest.pizzaTailleQteList().size(); i++) {
            if (commandeRequest.pizzaTailleQteList().get(i).idPizza() == null) {
                message = "L'Id de la Pizza est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }
            if (commandeRequest.pizzaTailleQteList().get(i).taille() == null) {
                message = "La taille est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }
            if (commandeRequest.pizzaTailleQteList().get(i).quantite() == null) {
                message = "La quantite est obligatoire";
                log.error(ERREUR_VERIFICATION_COMMANDE, message);
                throw new CommandeException(message);
            }

        }
    }


    private Commande toCommande(CommandeRequestDto commandeRequestDto) {
        Commande commande = new Commande();
        commande.setClient(
                clientDao.findById(
                                commandeRequestDto.idClient())
                        .orElseThrow(() -> new EntityNotFoundException("Le client n'existe pas en base")));
        commande.setPizzaTailleQteList(
                commandeRequestDto.pizzaTailleQteList().stream()
                        .map(this::toPizzaTailleQte)
                        .toList());

        return commande;
    }


    private CommandeResponseDto toCommandeResponse(Commande commande) {
        return new CommandeResponseDto(commande.getId()
                , commande.getClient().getId()
                ,commande.getPizzaTailleQteList().stream().map(this::toPizzaTailleQteResponseDto).toList());
    }

    private PizzaTailleQte toPizzaTailleQte(PizzaTailleQteRequestDto pizzaTailleQteRequestDto) {
        PizzaTailleQte pizzaTailleQte = new PizzaTailleQte();
        pizzaTailleQte.setPizza(
                pizzaDao.findById(pizzaTailleQteRequestDto.idPizza())
                        .orElseThrow(() -> new EntityNotFoundException("La pizza n'existe pas en base")));
        pizzaTailleQte.setTaille(pizzaTailleQteRequestDto.taille());
        pizzaTailleQte.setQuantite(pizzaTailleQteRequestDto.quantite());
        return pizzaTailleQte;
    }

    private PizzaTailleQteResponseDto toPizzaTailleQteResponseDto( PizzaTailleQte pizzaTailleQte){
        return new PizzaTailleQteResponseDto(pizzaTailleQte.getPizza().getNom(), pizzaTailleQte.getTaille(), pizzaTailleQte.getQuantite());
    }


}
