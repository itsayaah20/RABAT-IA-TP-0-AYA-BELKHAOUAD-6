package ma.emsi.belkhaouad.tp0.service;

import jakarta.enterprise.context.Dependent;
import java.io.Serializable;

/**
 * Génère la réponse à la question de l'utilisateur.
 * Dans les TPs suivants, cette classe appellera l'API d'un LLM.
 * Portée Dependent : l'instance vit aussi longtemps que le backing bean dans lequel elle est injectée.
 *
 * Traitement personnel : chiffrement de César dont le décalage est le numéro de la question
 * dans la conversation (1ère question : décalage 1, 2ème : décalage 2, etc.).
 * Seules les lettres A-Z sont décalées (les accents et les autres caractères sont conservés).
 * Le nombre de mots de la question est ajouté à la fin de la réponse.
 */
@Dependent
public class GenerateurReponse implements Serializable {

    private boolean premiereQuestion = true;
    private int numeroQuestion = 0;

    public String genererReponse(String question, String roleSysteme) {
        numeroQuestion++;
        StringBuilder chiffre = new StringBuilder();
        for (char c : question.toCharArray()) {
            chiffre.append(decaler(c, numeroQuestion));
        }
        int nbMots = question.trim().split("\\s+").length;
        String reponse = chiffre.toString();
        if (premiereQuestion) {
            reponse = roleSysteme.toUpperCase() + "\n" + reponse;
            premiereQuestion = false;
        }
        return "||" + reponse + "|| (" + nbMots + " mot" + (nbMots > 1 ? "s" : "")
                + ", décalage " + numeroQuestion + ")";
    }

    /** Décale une lettre non accentuée de n positions dans l'alphabet ; laisse les autres caractères. */
    private char decaler(char c, int n) {
        if (c >= 'a' && c <= 'z') {
            return (char) ('a' + (c - 'a' + n) % 26);
        }
        if (c >= 'A' && c <= 'Z') {
            return (char) ('A' + (c - 'A' + n) % 26);
        }
        return c;
    }
}
