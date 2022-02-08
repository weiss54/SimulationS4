public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */

    public EvenementOuverturePorteCabine(long d) {
        super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("OPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Cabine cabine = immeuble.cabine;
        Etage étage = cabine.étage;
        assert !cabine.porteOuverte;

		long tempsACompter = tempsPourOuvrirOuFermerLesPortes;
		cabine.porteOuverte = true;
        int nbPassagersDescendus = cabine.faireDescendrePassagers(immeuble, date);
		tempsACompter += nbPassagersDescendus*tempsPourEntrerOuSortirDeLaCabine;

        if (cabine.cabineEstVide() && !immeuble.passagerAuDessus(étage) && !immeuble.passagerEnDessous(étage) && !étage.aDesPassagers()) {
            // si personne dans l'immeuble, lintention devient -
            cabine.changerIntention('-');
        } else {
            if (cabine.intention() == 'v' && !immeuble.passagerEnDessous(étage) && cabine.cabineEstVide())
                cabine.changerIntention('-');
            if (cabine.intention() == '^' && !immeuble.passagerAuDessus(étage) && cabine.cabineEstVide())
                cabine.changerIntention('-');
            // on fait monter ceux qui attendent
            if (étage.aDesPassagers()) {
                if (!modeParfait) { // mode infernal
                    while (étage.aDesPassagers() && !cabine.cabinePleine()) {
                        Passager p = étage.enleverProchainPassager();
                        if (cabine.cabineEstVide()) cabine.changerIntention(p.sens());
                        cabine.faireMonterPassager(p);
                        tempsACompter += Global.tempsPourEntrerOuSortirDeLaCabine;
                    }
                } else { // mode parfait
                    boolean pEnAttente = étage.aDesPassagers();
                    while (pEnAttente && !cabine.cabinePleine()) {
                        if (cabine.intention() == '-') {
                            Passager p = étage.enleverProchainPassager();
                            cabine.changerIntention(p.sens());
                            cabine.faireMonterPassager(p);
						} else
                        if (cabine.intention() == '^' && étage.aDesPassagersQuiMontent()) {
                            cabine.faireMonterPassager(étage.enleverProchainPassagerQuiMontent());
                        } else
                        if (cabine.intention() == 'v' && étage.aDesPassagersQuiDescendent()) {
                            cabine.faireMonterPassager(étage.enleverProchainPassagerQuiDescendent());
                        }
						if (cabine.intention() == '^') pEnAttente = étage.aDesPassagersQuiMontent(); else pEnAttente = étage.aDesPassagersQuiDescendent();
                        tempsACompter += Global.tempsPourEntrerOuSortirDeLaCabine;
                    }
                }
            }
            echeancier.ajouter(new EvenementFermeturePorteCabine(date + tempsACompter));
        }

        assert cabine.porteOuverte;
    }

}
