import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffFavesComponent } from './staff-faves.component';

describe('StaffFavesComponent', () => {
  let component: StaffFavesComponent;
  let fixture: ComponentFixture<StaffFavesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StaffFavesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffFavesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
