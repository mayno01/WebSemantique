export enum TypeReclamation {
    ERREURS_DE_COMMANDE = 'ERREURS_DE_COMMANDE',
    PROBLEMES_DE_QUALITE_DES_PLANTES = 'PROBLEMES_DE_QUALITE_DES_PLANTES',
    PROBLEMES_LIES_AUX_PROMOTIONS = 'PROBLEMES_LIES_AUX_PROMOTIONS',
    PRODUITS_DE_FECTUEUX = 'PRODUITS_DE_FECTUEUX',
    SERVICE_CLIENTELE_INSATISFAISANT='SERVICE_CLIENTELE_INSATISFAISANT'
  }
  
  export interface Reclamation {
    id: string;
    title: string;
    description: string;
    date: string;
    type: TypeReclamation;
  }
  