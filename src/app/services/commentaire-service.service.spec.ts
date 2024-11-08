import { TestBed } from '@angular/core/testing';

import { CommentaireServiceService } from './commentaire-service.service';

describe('CommentaireServiceService', () => {
  let service: CommentaireServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentaireServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
