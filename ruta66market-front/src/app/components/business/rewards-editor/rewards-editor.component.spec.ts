import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RewardsEditorComponent } from './rewards-editor.component';

describe('RewardsEditorComponent', () => {
  let component: RewardsEditorComponent;
  let fixture: ComponentFixture<RewardsEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RewardsEditorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RewardsEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
