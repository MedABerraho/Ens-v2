import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { FormsModule } from '@angular/forms';
import { SourceComponent } from './source/source.component';
import { BourceComponent } from './bource/bource.component';
import { DepenseComponent } from './depense/depense.component';
import { LoginComponent } from './login/login.component';
import { SingupComponent } from './singup/singup.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'source', component: SourceComponent },
  { path: 'bource', component: BourceComponent },
  { path: 'depense', component: DepenseComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SingupComponent }

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    FormsModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
