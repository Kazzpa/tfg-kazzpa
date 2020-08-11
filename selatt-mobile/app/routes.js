import LoginView from "./components/LoginView";
import ProfileView from "./components/ProfileView";
import LandingView from "./components/LandingView";
import AlgorithmsView from "./components/AlgorithmsView";
import ResultsView from "./components/ResultsView";

export const routes = {
    'login': {
        component: LoginView
    },
    'profile': {
        component: ProfileView
    },
    '/': {
        component: LandingView
    },
    'algorithms': {
        component: AlgorithmsView
    },
    'results': {
        component: ResultsView
    }
}