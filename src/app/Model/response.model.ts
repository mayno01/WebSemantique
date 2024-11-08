export enum TypeResponse {
    MESSAGE = 'MESSAGE',
    TELEPHONIQUE = 'TELEPHONIQUE',
    VISITE = 'VISITE',
  }
  
  export interface Response {
    id: string;
    reclamationId: string;
    responseText: string;
    date: string;
    type: TypeResponse;
  }
  