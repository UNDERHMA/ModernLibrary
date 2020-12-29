import { BrowserModule, } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ApiService } from '../api.service'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ProfileComponent } from './profile/profile.component';
import { EventsComponent } from './events/events.component';
import { CartComponent } from './cart/cart.component';
import { SignupComponent } from './signup/signup.component';
import { EditprofileComponent } from './editprofile/editprofile.component';
import { LoginComponent } from './login/login.component';
import { BooksComponent } from './books/books.component';
import { ReservedComponent } from './reserved/reserved.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { LogoutComponent } from './logout/logout.component';
import { StaffFavesComponent } from './staff-faves/staff-faves.component';
import { ContactComponent } from './contact/contact.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    ProfileComponent,
    EventsComponent,
    CartComponent,
    SignupComponent,
    EditprofileComponent,
    LoginComponent,
    BooksComponent,
    ReservedComponent,
    WishlistComponent,
    LogoutComponent,
    StaffFavesComponent,
    ContactComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
   
  ],
  providers: [ApiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
