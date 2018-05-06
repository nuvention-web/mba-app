import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng2FileInputModule } from 'ng2-file-input';

import { FeedbackComponent } from './feedback.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {FeedbackService} from "../../../../_services/feedback.service";

import { RouterModule, Routes } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import {FeedbackdefaultComponent} from "./feedbackdefault.component";


const routes: Routes = [
    {
        'path': "",
        'component': FeedbackdefaultComponent,
        'children': [
            {
                'path': "",
                'component': FeedbackComponent,
            },
        ],
    },
];
@NgModule({
  declarations: [
    FeedbackComponent, FeedbackdefaultComponent
  ],
  imports: [
    CommonModule, NgbModule.forRoot(), HttpClientModule, FormsModule,
    Ng2FileInputModule.forRoot(), HttpModule, RouterModule.forChild(routes), LayoutModule
  ],
  providers: [FeedbackService],
  bootstrap: [FeedbackComponent]
})
export class FeedbackModule { }
