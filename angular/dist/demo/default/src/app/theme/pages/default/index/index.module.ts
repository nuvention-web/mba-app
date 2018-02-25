import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { IndexComponent } from './index.component';
import { LayoutModule } from '../../../layouts/layout.module';
import { DefaultComponent } from '../default.component';
import { ValidationFormControlsComponent } from '../components/forms/validation/validation-form-controls/validation-form-controls.component';
import { LayoutsDefaultFormsComponent } from '../components/forms/layouts/layouts-default-forms/layouts-default-forms.component';
import { ControlsInputGroupComponent } from '../components/forms/controls/controls-input-group/controls-input-group.component';
import { LayoutsMultiColumnFormsComponent } from '../components/forms/layouts/layouts-multi-column-forms/layouts-multi-column-forms.component';
import { LayoutsActionBarsComponent } from '../components/forms/layouts/layouts-action-bars/layouts-action-bars.component';
import { WidgetsBootstrapSelectComponent } from '../components/forms/widgets/widgets-bootstrap-select/widgets-bootstrap-select.component';
import { WidgetsBootstrapSelectModule } from '../components/forms/widgets/widgets-bootstrap-select/widgets-bootstrap-select.module';
import { WidgetsBootstrapSwitchComponent } from '../components/forms/widgets/widgets-bootstrap-switch/widgets-bootstrap-switch.component';
import { UsersSerivce } from '../../../../_services/users.service';
import { FormsModule } from '@angular/forms';


const routes: Routes = [
    {
        "path": "",
        "component": DefaultComponent,
        "children": [
            {
                "path": "",
                "component": IndexComponent
            }
        ]
    }
];
@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule, 
        WidgetsBootstrapSelectModule, FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        IndexComponent,ValidationFormControlsComponent,
        LayoutsDefaultFormsComponent,ControlsInputGroupComponent
    ], providers: [
        UsersSerivce
    ] 
})
export class IndexModule {



}