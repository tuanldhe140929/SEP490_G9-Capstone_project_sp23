import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SellerViewsRoutingModule } from './seller-views-routing.module';
import { UpdateProductComponent } from './update-product/update-product.component';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { VgCoreModule } from '@videogular/ngx-videogular/core';
import { VgControlsModule } from '@videogular/ngx-videogular/controls';
import { VgOverlayPlayModule } from '@videogular/ngx-videogular/overlay-play';
import { VgBufferingModule } from '@videogular/ngx-videogular/buffering';
import { SellerGuardGuard } from '../../guards/seller-guard.guard';
import { DecimalPipe } from '@angular/common';
import { PayoutHistoryComponent } from './payout-history/payout-history.component';
import { CommonViewsModule } from "../common-views/common-views.module";
import { NgxPaginationModule } from 'ngx-pagination';
import { ApplyLicenseComponent } from './update-product/apply-license/apply-license.component';
import { ConfirmComponent } from './update-product/apply-license/confirm/confirm.component';

@NgModule({
    declarations: [
        UpdateProductComponent,
        PayoutHistoryComponent,
        ApplyLicenseComponent,
        ConfirmComponent
    ],
    providers: [SellerGuardGuard, DecimalPipe],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        SellerViewsRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        MatIconModule,
        MatProgressBarModule,
        MatAutocompleteModule,
        MatFormFieldModule,
        CKEditorModule,
        VgCoreModule,
        VgControlsModule,
        VgOverlayPlayModule,
        VgBufferingModule,
        CommonViewsModule,
        NgxPaginationModule
    ]
})
export class SellerViewsModule { }
