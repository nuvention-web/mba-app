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
    signupEmail: string;
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
        let email = this.model.email;
        if (this.signupEmail != null)
            email = this.signupEmail;
        this._authService.login(email, this.model.password).subscribe(
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
            this._alertService.error('The passwords you entered do not match.');
            this.loading = false;
            return;
        }
        this.loading = false;
        this._userService.create(this.model).subscribe(
            data => {
                this.showAlert('alertVerify');
                this._alertService.success(
                    'Thank you! An activation code has been sent to your email. Please enter the code below to complete creating your account.',
                    true);
                this.loading = false;
                LoginCustom.displayVerificationCode();
                this.signupEmail = this.model.email;
                // this.model = {};
            },
            error => {
                this.showAlert('alertSignup');
                this._alertService.error(error);
                this.loading = false;
            });
    }

    verify() {
        this.loading = true;
        this._userService.verify(this.signupEmail, this.model.code).subscribe(
            data => {
                this.showAlert('alertSignin');
                this._alertService.success(
                    'Congratulation! You have created your account successfully!',
                    true);
                this.loading = false;
                this.signin();
                // LoginCustom.displaySignInForm();
                // this.model = {};
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
                    'A password reset code has been sent to your email, you will need to provide the code to reset your password.',
                    true);
                this.loading = false;
                LoginCustom.displayResetPasswordForm();
                // this.model = {};
            },
            error => {
                this.showAlert('alertForgotPass');
                this._alertService.error(error);
                this.loading = false;
            });
    }

    resetPass() {
        this.loading = true;

        if (this.model.new_pass!== this.model.new_pass_confirm) {
            this.showAlert('alertResetPass');
            this._alertService.error('The passwords you entered do not match.');
            this.loading = false;
            return;
        }
        console.log(this.model.email);
        this._userService.resetPassword(this.model.email, this.model.code, this.model.new_pass).subscribe(
            data => {
                this.showAlert('alertSignin');
                this._alertService.success(
                    'You have successfully reset your password! Please sign in with the new password.',
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