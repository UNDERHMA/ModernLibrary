import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BooksComponent } from './books/books.component';
import { CartComponent } from './cart/cart.component';
import { EditprofileComponent } from './editprofile/editprofile.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { ProfileComponent } from './profile/profile.component';
import { ReservedComponent } from './reserved/reserved.component';
import { SignupComponent } from './signup/signup.component';
import { WishlistComponent } from './wishlist/wishlist.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomepageComponent},
  { path: 'profile', component: ProfileComponent},
  { path: 'edit-profile', component: EditprofileComponent},
  { path: 'cart', component: CartComponent},
  { path: 'signup', component: SignupComponent},
  { path: 'login', component: LoginComponent},
  { path: 'books', component: BooksComponent},
  { path: 'reserved', component: ReservedComponent },
  { path: 'wishlist', component: WishlistComponent },
  { path: 'log-out', component: LogoutComponent}

  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
