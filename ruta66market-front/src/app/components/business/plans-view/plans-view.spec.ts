import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlansView } from './plans-view';

describe('PlansView', () => {
  let component: PlansView;
  let fixture: ComponentFixture<PlansView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlansView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlansView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
