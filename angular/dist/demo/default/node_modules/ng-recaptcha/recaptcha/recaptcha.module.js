"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var recaptcha_common_module_1 = require("./recaptcha-common.module");
var recaptcha_loader_service_1 = require("./recaptcha-loader.service");
var recaptcha_component_1 = require("./recaptcha.component");
var RecaptchaModule = (function () {
    function RecaptchaModule() {
    }
    RecaptchaModule.forRoot = function () {
        return {
            ngModule: RecaptchaModule,
            providers: [
                recaptcha_loader_service_1.RecaptchaLoaderService,
            ],
        };
    };
    RecaptchaModule.decorators = [
        { type: core_1.NgModule, args: [{
                    exports: [recaptcha_component_1.RecaptchaComponent],
                    imports: [recaptcha_common_module_1.RecaptchaCommonModule],
                },] },
    ];
    /** @nocollapse */
    RecaptchaModule.ctorParameters = function () { return []; };
    return RecaptchaModule;
}());
exports.RecaptchaModule = RecaptchaModule;
