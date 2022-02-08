public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */
    
    private Etage étage;
    
    public EvenementPassageCabinePalier(long d, Etage e) {
	super(d);
	étage = e;
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("PCP ");
	buffer.append(étage.numéro());
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine cabine = immeuble.cabine;
		assert ! cabine.porteOuverte;
		assert étage.numéro() != cabine.étage.numéro();

		cabine.étage = étage;
		Etage etage = cabine.étage;

		if (étage.aDesPassagers()) {
			echeancier.ajouter(new EvenementOuverturePorteCabine(date+Global.tempsPourOuvrirOuFermerLesPortes));
		} else {
			// si personne ne veut descendre
			if (!cabine.passagersVeulentDescendre()) {

				// si la cabine veut monter, on passe a letage sup
				if (cabine.intention() == '^') {
					if (étage.numéro() < immeuble.étageLePlusHaut().numéro()) {
						etage = immeuble.étage(etage.numéro() + 1);
					} else {
						cabine.changerIntention('-');
					}
					// si la cabine veu descendre on passe l'etage en dessous
				} else {
					if (étage.numéro() > immeuble.étageLePlusBas().numéro()) {
						etage = immeuble.étage(etage.numéro() - 1);
					} else {
						cabine.changerIntention('-');
					}
				}

				// on ajoute l'evenement passage cabine palier
				echeancier.ajouter(new EvenementPassageCabinePalier(date + Global.tempsPourBougerLaCabineDUnEtage, etage));

			} else { // si quelqu'un veut descendre
				echeancier.ajouter(new EvenementOuverturePorteCabine(date + Global.tempsPourOuvrirOuFermerLesPortes));
			}
		}



    }

}
