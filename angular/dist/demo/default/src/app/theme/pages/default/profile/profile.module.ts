import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { LayoutModule } from '../../../layouts/layout.module';
import { FormsModule } from '@angular/forms';
import { UsersSerivce } from '../../../../_services/users.service';
import { DefaultComponent } from '../default.component';

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
        , FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        ProfileComponent
    ], providers: [
        UsersSerivce
    ]
})
export class ProfileModule {



}