import {
    Component,
    ComponentFactoryResolver,
    OnInit,
    ViewChild,
    ViewContainerRef,
    ViewEncapsulation,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptLoaderService } from '../_services/script-loader.service';
import { AuthenticationService } from './_services/authentication.service';
import { AlertService } from './_services/alert.service';
import { UserService } from './_services/user.service';
import { AlertComponent } from './_directives/alert.component';
import { LoginCustom } from './_helpers/login-custom';
import { Helpers } from '../helpers';
// import { setUser } from "../_services/url-infos";

@Component({
    selector: '.m-grid.m-grid--hor.m-grid--root.m-page',
    templateUrl: './templates/login-1.component.html',
    encapsulation: ViewEncapsulation.None,
})

export class AuthComponent implements OnInit {
    model: any = {};
    loading = false;
    returnUrl: string;

    @ViewChild('alertSignin',
        { read: ViewContainerRef }) alertSignin: ViewContainerRef;
    @ViewChild('alertSignup',
        { read: ViewContainerRef }) alertSignup: ViewContainerRef;
    @ViewChild('alertForgotPass',
        { read: ViewContainerRef }) alertForgotPass: ViewContainerRef;
    @ViewChild('alertVerify',
        { read: ViewContainerRef }) alertVerify: ViewContainerRef;
    @ViewChild('alertResetPass',
        { read: ViewContainerRef }) alertResetPass: ViewContainerRef;

    constructor(
        private _router: Router,
        private _script: ScriptLoaderService,
        private _userService: UserService,
        private _route: ActivatedRoute,
        private _authService: AuthenticationService,
        private _alertService: AlertService,
        private cfr: ComponentFactoryResolver) {
    }

    ngOnInit() {
        this.model.remember = true;
        // get return url from route parameters or default to '/'
        this.returnUrl = this._route.snapshot.queryParams['returnUrl'] || '/';
        this._router.navigate([this.returnUrl]);

        this._script.loadScripts('body', [
            'assets/vendors/base/vendors.bundle.js',
            'assets/demo/default/base/scripts.bundle.js'], true).then(() => {
                Helpers.setLoading(false);
                LoginCustom.init();
            });
        this.model.email = "";
    }

    signin() {
        this.loading = true;
        this._authService.login(this.model.email, this.model.password).subscribe(
            data => {
                this._router.navigate([this.returnUrl]);
                // setUser(this.model.email);
            },
            error => {
                this.showAlert('alertSignin');
                this._alertService.error("Invalid username or password");
                this.loading = false;
            });
    }

    signup() {
        this.loading = true;
        if (this.model.password !== this.model.rpassword) {
            this.showAlert('alertSignup');
            this._alertService.error('Two passwords are not equal.');
            this.loading = false;
            return;
        }
        this.loading = false;
        this._userService.create(this.model).subscribe(
            data => {
                this.showAlert('alertVerify');
                this._alertService.success(
                    'Thank you.  The code has sent to your email. To complete your registration please fill in the code.',
                    true);
                this.loading = false;
                LoginCustom.displayVerificationCode();
                this.model = {};
            },
            error => {
                this.showAlert('alertSignup');
                this._alertService.error(error);
                this.loading = false;
            });
    }

    verify() {
        this.loading = true;
        this._userService.verify(this.model.email, this.model.code).subscribe(
            data => {
                this.showAlert('alertSignin');
                this._alertService.success(
                    'Congratulation! You have created your account successfully!',
                    true);
                this.loading = false;
                LoginCustom.displaySignInForm();
                this.model = {};
            },
            error => {
                this.showAlert('alertVerify');
                this._alertService.error(error);
                this.loading = false;
            });
    }

    forgotPass() {
        this.loading = true;
        this._userService.forgotPassword(this.model.email).subscribe(
            data => {
                this.showAlert('alertResetPass');
                this._alertService.success(
                    'Cool! Password reset code has been sent to your email.',
                    true);
                this.loading = false;
                LoginCustom.displayResetPasswordForm();
                this.model = {};
            },
            error => {
                this.showAlert('alertForgotPass');
                this._alertService.error(error);
                this.loading = false;
            });
    }

    resetPass() {
        this.loading = true;
        this._userService.resetPassword(this.model.email, this.model.code, this.model.new_pass).subscribe(
            data => {
                this.showAlert('alertSignin');
                this._alertService.success(
                    'You already reset your password. Now sign in with new password!',
                    true);
                this.loading = false;
                LoginCustom.displaySignInForm();
                this.model = {};
            },
            error => {
                this.showAlert('alertResetPass');
                this._alertService.error(error);
                this.loading = false;
            });

    }

    showAlert(target) {
        this[target].clear();
        let factory = this.cfr.resolveComponentFactory(AlertComponent);
        let ref = this[target].createComponent(factory);
        ref.changeDetectorRef.detectChanges();
    }
}