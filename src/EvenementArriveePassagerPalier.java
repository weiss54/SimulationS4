public class EvenementArriveePassagerPalier extends Evenement {
    /* APP: Arrivée Passager Palier
       L'instant précis ou un nouveau passager arrive sur un palier donné, dans le but
       de monter dans la cabine.
    */

    private Etage étage;

    public EvenementArriveePassagerPalier(long d, Etage edd) {
        super(d);
        étage = edd;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("APP ");
        buffer.append(étage.numéro());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        assert étage != null;
        assert immeuble.étage(étage.numéro()) == étage;
        Passager p = new Passager(date, étage, immeuble);
        Cabine c = immeuble.cabine;

        if (c.porteOuverte && c.étage == étage) {
            if (c.intention() == '-') {
                // On change la direction de l'ascenceur selon celui du passager
                c.changerIntention(p.sens());
                // On ajoute l'evenant de fermeture de la porte
                echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
                char fmp = c.faireMonterPassager(p);
                // Faudrait aussi ajouter le premier PCP...
                if (fmp == 'O') {
                    assert true;
                } else {
                    assert false : "else impossible";
                }
            } else {
                notYetImplemented();
            }
            ;
        } else {
            // Vue que la cabine n'est pas là, le passager s'ajoute à l'étage
            étage.ajouter(p);
            // dans le cas ou la cabine est vide et sans intention, le passager l'appel
            if (c.cabineEstVide() && c.intention()=='-') {
                c.changerIntention((p.étageDestination().numéro() > p.étageDépart().numéro() ? 'v' : '^'));
                echeancier.ajouter(new EvenementFermeturePorteCabine(date+Global.tempsPourOuvrirOuFermerLesPortes));
            }
            // on ajouter a l'echancier le PAP
            echeancier.ajouter(new EvenementPietonArrivePalier(date+Global.délaiDePatienceAvantSportif, p.étageDépart(), p));
        }
        // cas général on ajoute un APP pour le meme etage mais pas a la meme date
        this.date = étage.arrivéeSuivante()+date;
        echeancier.ajouter(this);

        assert c.intention() != '-' : "intention impossible";
    }

}
