package com.accenture.service.impl;

import com.accenture.exception.CommandeException;
import com.accenture.exception.IngredientException;
import com.accenture.exception.PizzaException;
import com.accenture.repository.ClientDao;
import com.accenture.repository.CommandeDao;
import com.accenture.repository.IngredientDao;
import com.accenture.repository.PizzaDao;
import com.accenture.repository.model.Commande;
import com.accenture.repository.model.Ingredient;
import com.accenture.repository.model.PizzaTailleQte;
import com.accenture.service.inter.CommandeService;
import com.accenture.service.dto.CommandeRequestDto;
import com.accenture.service.dto.CommandeResponseDto;
import com.accenture.service.dto.PizzaTailleQteRequestDto;
import com.accenture.service.dto.PizzaTailleQteResponseDto;
import com.accenture.shared.Statut;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommandeServiceImpl implements CommandeService {


    public static final String ERREUR_VERIFICATION_COMMANDE = "Erreur verification Commande : {}";
    private PizzaDao pizzaDao;
    private ClientDao clientDao;
    private CommandeDao commandeDao;
    private IngredientDao ingredientDao;

    /**
     * Ajoute une nouvelle pizzaTailleQteList dans la base données
     *
     * @param commandeRequestDto Objet contenant les informations de la pizzaTailleQteList à ajouter
     * @return un objet CommandeResponseDto qui représente la pizzaTailleQteList à ajouter
     */
    @Transactional
    @Override
    public CommandeResponseDto ajouter(CommandeRequestDto commandeRequestDto) throws CommandeException {
        // On vérifie les inputs de la commande RequestDto
        verificationCommande(commandeRequestDto);
        // On transforme la commandeRequest en Commande
        Commande commande = toCommande(commandeRequestDto);
        //Check le status de la pizza, au cas ou le client rentre une pizza retirée de la carte
        checkPizzaCommandable(commande);
        //Retire les ingrédients du stock
        retirerIngredientStock(commande);


        commande.setPrix(commande.calculerPrix());
        commande.setDate(LocalDate.now());
        commande.setStatut(Statut.EN_ATTENTE);
        commandeDao.save(commande);
        return toCommandeResponse(commande);
    }

    @Override
    public CommandeResponseDto trouverParId(int id) {
        Optional<Commande> commande = commandeDao.findById(id);
        if (commande.isEmpty()) {
            EntityNotFoundException entityNotFoundException = new EntityNotFoundException("La commande n'existe pas en base de donnée");
            log.error("Erreur Commande par Id : {}", entityNotFoundException.getMessage());
            throw entityNotFoundException;
        }
        return toCommandeResponse(commande.get());
    }

    @Override
    public List<CommandeResponseDto> trouverToutes() {
        return commandeDao.findAll().stream().map(this::toCommandeResponse).toList();
    }

    @Override
    public CommandeResponseDto modifierStatus(int id, Statut statut) {
        Optional<Commande> optional = commandeDao.findById(id);
        if (optional.isEmpty()) {
            EntityNotFoundException entityNotFoundException = new EntityNotFoundException("La commande n'existe pas en base de donnée");
            log.error("Erreur Commande par Id : {}", entityNotFoundException.getMessage());
            throw entityNotFoundException;
        }
        optional.get().setStatut(statut);
        return toCommandeResponse(commandeDao.save(optional.get()));
    }

    @Override
    public List<CommandeResponseDto> trouverParStatut(Statut statut) {
      return commandeDao.findByStatut(statut).stream().map(this::toCommandeResponse).toList();
    }


    //    *************************************************************************
    //    ************************ METHODES PRIVEES *******************************
    //    *************************************************************************


    private void checkPizzaCommandable(Commande commande) {
        commande.getPizzaTailleQteList().forEach(this::verifierPizza);
    }

    private void verifierPizza(PizzaTailleQte pizzaTailleQte) {
        if (pizzaTailleQte.getPizza().getCommandable() == null || !pizzaTailleQte.getPizza().getCommandable()) {
            PizzaException pizzaException = new PizzaException("La pizza n'est pas commandable : " + pizzaTailleQte.getPizza().getNom());
            log.error(ERREUR_VERIFICATION_COMMANDE, pizzaException.getMessage());
            throw pizzaException;
        }
    }

    private void retirerIngredientStock(Commande commande) {
        commande.getPizzaTailleQteList().forEach(this::retirerIngredientPizzaTailleQte);
    }

    private void retirerIngredientPizzaTailleQte(PizzaTailleQte pizzaTailleQte) {
        for (int i = 1; i <= pizzaTailleQte.getQuantite(); i++) {
            pizzaTailleQte.getPizza().getIngredients().forEach(this::decrementerIngredient);
        }
    }

    private void decrementerIngredient(Ingredient ingredient) {
        Ingredient ingredientEnBase = ingredientDao.findById(ingredient.getId()).orElseThrow(() -> new EntityNotFoundException("Ingrédient Not Found"));
        if (ingredientEnBase.getQuantite() < 1) {
            String message = "Ingrédient Insuffisant " + ingredientEnBase.getNom();
            log.error(ERREUR_VERIFICATION_COMMANDE, message);
            throw new IngredientException(message);
        }
        ingredientEnBase.setQuantite(ingredientEnBase.getQuantite() - 1);

    }


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
        return new CommandeResponseDto(commande.getId(),
                commande.getClient().getId(),
                commande.getPizzaTailleQteList().stream().map(this::toPizzaTailleQteResponseDto).toList(),
                commande.getDate(),
                commande.getStatut(),
                commande.getPrix());
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

    private PizzaTailleQteResponseDto toPizzaTailleQteResponseDto(PizzaTailleQte pizzaTailleQte) {
        return new PizzaTailleQteResponseDto(pizzaTailleQte.getPizza().getNom(), pizzaTailleQte.getTaille(), pizzaTailleQte.getQuantite());
    }


}
