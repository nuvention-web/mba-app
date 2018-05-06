import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LogoutComponent } from "./auth/logout/logout.component";
import { WidgetsBootstrapSelectComponent } from './theme/pages/default/components/forms/widgets/widgets-bootstrap-select/widgets-bootstrap-select.component';

const routes: Routes = [
    { path: 'signin', loadChildren: './auth/auth.module#AuthModule' },
    { path: 'logout', component: LogoutComponent },
    { path: '', redirectTo: 'index.html', pathMatch: 'full' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }