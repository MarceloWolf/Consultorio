import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSpecialitiesDialogComponent } from './view-specialities-dialog.component';

describe('ViewSpecialitiesDialogComponent', () => {
  let component: ViewSpecialitiesDialogComponent;
  let fixture: ComponentFixture<ViewSpecialitiesDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewSpecialitiesDialogComponent]
    });
    fixture = TestBed.createComponent(ViewSpecialitiesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
