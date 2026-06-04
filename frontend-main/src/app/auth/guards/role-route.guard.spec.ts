import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { roleRouteGuard } from './role-route.guard';

describe('roleRouteGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => roleRouteGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
