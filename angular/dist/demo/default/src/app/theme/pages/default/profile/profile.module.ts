import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { LayoutModule } from '../../../layouts/layout.module';
import { FormsModule } from '@angular/forms';
import { UsersService } from '../../../../_services/users.service';
import { DefaultComponent } from '../default.component';
import { GoalsComponent } from './goals/goals.component';
import { ExperiencesComponent } from './experiences/experiences.component';
import { ProfileService } from '../../../../_services/profile.service';
import { InterestsComponent } from './interests/interests.component';
import { InputEditorModule } from 'angular-inline-editors';
import { TextAreaEditorModule } from 'angular-inline-editors';
import {OverviewComponent} from "./overview/overview.component";

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': ProfileComponent,
            },
        ],
    },
];


@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule
        , FormsModule, InputEditorModule.forRoot(), TextAreaEditorModule.forRoot()
    ], exports: [
        RouterModule
    ], declarations: [
        ProfileComponent,
        GoalsComponent,
        ExperiencesComponent,
        OverviewComponent,
        InterestsComponent
    ], providers: [
        UsersService, ProfileService
    ]
})
export class ProfileModule {



}