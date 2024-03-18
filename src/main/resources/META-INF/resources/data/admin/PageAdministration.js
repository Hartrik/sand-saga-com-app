import DomBuilder from "./DomBuilder.js";
import ComponentStatsByDay from "./ComponentStatsByDay.js";
import ComponentStatsByScenario from "./ComponentStatsByScenario.js";
import ComponentUsers from "./ComponentUsers.js";
import ComponentCompleted from "./ComponentCompleted.js";
import ComponentReports from "./ComponentReports.js";
import { handle } from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */


const root = document.getElementById('administration-root');

const componentStatsByDay = new ComponentStatsByDay();
const componentStatsByScenario = new ComponentStatsByScenario();
const componentReports = new ComponentReports()
const componentCompleted = new ComponentCompleted();
const componentUsers = new ComponentUsers();


// server status

const statusPar = DomBuilder.par({style: 'font-family: Consolas, monospace;'});
root.append(statusPar);
handle(false, false, fetch('/api/admin/server-status', {
    method: 'GET',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
}), result => {
    const json = JSON.parse(result);
    statusPar.innerText = Object.entries(json).map(([k, v]) => k + ': ' + v).join(', ');
});

// buttons

root.append(DomBuilder.par(null, [
    DomBuilder.button('Reload Config', {class: 'btn btn-primary'}, () => {
        handle(true, true, fetch('/api/admin/reload-config', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ''
        }));
    }),
    ' ',
    DomBuilder.button('Reload Texts', {class: 'btn btn-primary'}, () => {
        handle(true, true, fetch('/api/admin/reload-texts', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ''
        }));
    }),
    ' ',
    DomBuilder.button('Refresh Live Stats', {class: 'btn btn-primary'}, () => {
        handle(true, true, fetch('/api/admin/refresh-live-stats', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ''
        }));
    }),
    ' ',
    DomBuilder.button('Update Stats From Completed', {class: 'btn btn-primary'}, () => {
        handle(true, true, fetch('/api/admin/update-stats-from-completed', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: ''
        }), () => {
            componentStatsByScenario.refresh();
            componentStatsByDay.refresh();
        });
    })
]));

root.append(DomBuilder.element('h3', null, 'Statistics'));
root.append(componentStatsByScenario.createNode());
root.append(componentStatsByDay.createNode());

root.append(DomBuilder.element('h3', null, 'Reported'));
root.append(componentReports.createNode());

root.append(DomBuilder.element('h3', null, 'Completed'));
root.append(componentCompleted.createNode());

root.append(DomBuilder.element('h3', null, 'User list'));
root.append(componentUsers.createNode());

componentStatsByScenario.refresh();
componentStatsByDay.refresh();
componentReports.refresh();
componentCompleted.refresh();
componentUsers.refresh();
