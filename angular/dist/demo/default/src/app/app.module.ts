import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ThemeComponent } from './theme/theme.component';
import { LayoutModule } from './theme/layouts/layout.module';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ScriptLoaderService } from "./_services/script-loader.service";
import { ThemeRoutingModule } from "./theme/theme-routing.module";
import { AuthModule } from "./auth/auth.module";
import { IconFlaticonModule } from './theme/pages/default/components/buttons/icon/icon-flaticon/icon-flaticon.module';
import { IconFlaticonComponent } from './theme/pages/default/components/buttons/icon/icon-flaticon/icon-flaticon.component';
import { WidgetsBootstrapSelectComponent } from './theme/pages/default/components/forms/widgets/widgets-bootstrap-select/widgets-bootstrap-select.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    declarations: [
        ThemeComponent,
        AppComponent,
    ],
    imports: [
        LayoutModule,
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        ThemeRoutingModule,
        AuthModule,
        FormsModule
    ],
    providers: [ScriptLoaderService],
    bootstrap: [AppComponent]
})
export class AppModule { }