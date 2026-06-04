import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecretaryViewComponent } from './secretary-view.component';

describe('SecretaryViewComponent', () => {
  let component: SecretaryViewComponent;
  let fixture: ComponentFixture<SecretaryViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SecretaryViewComponent]
    });
    fixture = TestBed.createComponent(SecretaryViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
