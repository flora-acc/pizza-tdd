INSERT INTO INGREDIENTS (nom, quantite) VALUES
('Emmental', 4),
('Ananas', 5);

INSERT INTO PIZZAS (nom, commandable) VALUES
('Hawaienne', true);

INSERT INTO PIZZAS_INGREDIENTS (pizza_id, ingredients_id) VALUES
(1,1),
(1,2);

INSERT INTO PIZZAS_PRIX (pizza_id, taille, prix) VALUES
(1, 'PETITE', 10.00),
(1, 'MOYENNE', 12.00),
(1, 'GRANDE', 14.00),
(2, 'PETITE', 11.00),
(2, 'MOYENNE', 13.00),
(2, 'GRANDE', 15.00);
(1, 'GRANDE', 14.00);

INSERT INTO CLIENT (nom, prenom, email) VALUES
('Michel', 'Durand', 'michel@email.fr'),
('Bob', 'McCain', 'bob@email.fr');

INSERT INTO COMMANDE (id) VALUES
(1),
(2);

INSERT INTO CLIENT_HISTORIQUE_COMM (client_id, historique_comm_id) VALUES
(1,1),
(2,2);