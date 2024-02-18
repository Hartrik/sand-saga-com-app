import { DomBuilder } from "/data/admin/DomBuilder.js";
import { formatDate } from "/data/admin/Utils.js";

/**
 * @version 2024-02-18
 * @author Patrik Harag
 */


const root = document.getElementById('administration-root');

function handle(alertOnSuccess, alertOnFailure, fetchPromise, onSuccess = undefined) {
    fetchPromise.then(r => {
        if (r.ok) {
            return r.text();
        } else {
            throw r.status
        }
    }).then(r => {
        if (alertOnSuccess) {
            alert('Success');
        }
        if (onSuccess !== undefined) {
            onSuccess(r);
        }
    }).catch(e => {
        if (alertOnFailure) {
            alert('Error: ' + e);
        }
    });
}

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
    })
]));


// stats

const statsByScenarioDiv = DomBuilder.div();
const statsByDayDiv = DomBuilder.div();
root.append(DomBuilder.element('h3', null, 'Statistics'));
root.append(statsByScenarioDiv);
root.append(statsByDayDiv);

function refreshStats() {
    fetch('/api/admin/stats/by-day', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(statsList => {
        const table = DomBuilder.bootstrapTableBuilder();

        function asDate(epochDay) {
            return new Date(epochDay * 86_400_000 + 10_000_000).toUTCString().substring(0, 17);
        }

        table.addRow(DomBuilder.element('tr', null, [
            DomBuilder.element('th', null, 'Id'),
            DomBuilder.element('th', null, 'Day'),
            DomBuilder.element('th', null, 'Updates'),
        ]));

        for (const stats of statsList) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + stats.id),
                DomBuilder.element('td', null, asDate(stats.id)),
                DomBuilder.element('td', null, '' + stats.updates)
            ]));
        }

        const total = DomBuilder.par();

        fetch('/api/admin/stats/sum', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(stats => {
            total.append(DomBuilder.span('Total updates (day updates sum): ' + stats.updates));
        });

        statsByDayDiv.innerHTML = '';
        statsByDayDiv.append(total);
        statsByDayDiv.append(table.createNode());
    });

    fetch('/api/admin/stats/by-scenario', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(scenarios => {
        const table = DomBuilder.bootstrapTableBuilder();

        table.addRow(DomBuilder.element('tr', null, [
            DomBuilder.element('th', null, 'Id'),
            DomBuilder.element('th', null, 'Name'),
            DomBuilder.element('th', null, 'Updates'),
        ]));

        for (const stats of scenarios) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + stats.id),
                DomBuilder.element('td', null, stats.name),
                DomBuilder.element('td', null, '' + stats.updates)
            ]));
        }

        statsByScenarioDiv.innerHTML = '';
        statsByScenarioDiv.append(table.createNode());
    });
}

refreshStats();


// reports

const reportDiv = DomBuilder.div();
root.append(DomBuilder.element('h3', null, 'Reported'));
root.append(reportDiv);

function refreshReports() {
    fetch('/api/admin/reports', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(reports => {
        const table = DomBuilder.bootstrapTableBuilder();

        function snapshotColumn(report) {
            if (report.snapshotSize === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.span('' + report.snapshotSize + ' bytes ', {
                    style: 'white-space: nowrap;'
                }),
                DomBuilder.element('br'),
                DomBuilder.element('a', {
                    href: `/api/admin/reports/${report.id}/snapshot.sgjs`,
                    target: '_blank'
                }, 'Download'),
                ' ',
                DomBuilder.element('a', {
                    href: `/admin/reports/${report.id}/snapshot.sgjs`,
                    target: '_blank'
                }, 'Play')
            ];
        }

        function metadataColumn(report) {
            if (report.metadata === null || report.metadata === undefined || report.metadata.length === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.link('Show', null, () => {
                    let dialog = DomBuilder.bootstrapDialogBuilder();
                    dialog.setHeaderContent('Metadata');
                    dialog.setBodyContent([
                        DomBuilder.par(null, report.metadata)
                    ]);
                    dialog.addCloseButton('Close');
                    dialog.show(root);
                })
            ];
        }

        function actionsColumn(report) {
            return DomBuilder.link('Delete', null, () => {
                handle(false, true, fetch('/api/admin/reports/' + report.id, {
                    method: 'DELETE',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                }), result => {
                    refreshReports();
                });
            })
        }

        table.addRow(DomBuilder.element('tr', null, [
            DomBuilder.element('th', null, 'ID'),
            DomBuilder.element('th', null, 'Date'),
            DomBuilder.element('th', null, 'Scenario'),
            DomBuilder.element('th', null, 'Type'),
            DomBuilder.element('th', null, 'Details'),
            DomBuilder.element('th', null, 'Metadata'),
            DomBuilder.element('th', null, 'Snapshot'),
            DomBuilder.element('th', null, 'IP'),
            DomBuilder.element('th', null, '_'),
        ]));

        for (const report of reports) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + report.id),
                DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(report.time)), {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, DomBuilder.span(report.scenario, {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, report.location),
                DomBuilder.element('td', null, [
                    DomBuilder.par(null, report.message)
                ]),
                DomBuilder.element('td', null, metadataColumn(report)),
                DomBuilder.element('td', null, snapshotColumn(report)),
                DomBuilder.element('td', null, report.ip),
                DomBuilder.element('td', null, actionsColumn(report)),
            ]));
        }

        reportDiv.innerHTML = '';
        reportDiv.append(table.createNode());
    });
}

refreshReports();


// completed

const completedDiv = DomBuilder.div();
root.append(DomBuilder.element('h3', null, 'Completed'));
root.append(completedDiv);

function refreshCompleted() {
    fetch('/api/admin/completed', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    }).then(response => response.json()).then(completedList => {
        const table = DomBuilder.bootstrapTableBuilder();

        function snapshotColumn(completed) {
            if (completed.snapshotSize === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.span('' + completed.snapshotSize + ' bytes ', {
                    style: 'white-space: nowrap;'
                }),
                DomBuilder.element('br'),
                DomBuilder.element('a', {
                    href: `/api/admin/completed/${completed.id}/snapshot.sgjs`,
                    target: '_blank'
                }, 'Download'),
                ' ',
                DomBuilder.element('a', {
                    href: `/admin/completed/${completed.id}/snapshot.sgjs`,
                    target: '_blank'
                }, 'Play'),
                ' ',
                DomBuilder.link('X', null, () => {
                    handle(false, true, fetch(`/api/admin/completed/${completed.id}/snapshot.sgjs`, {
                        method: 'DELETE',
                    }), result => {
                        refreshCompleted();
                    });
                })
            ];
        }

        function metadataColumn(completed) {
            if (completed.metadata === null || completed.metadata === undefined || completed.metadata.length === 0) {
                return 'n/a';
            }

            return [
                DomBuilder.link('Show', null, () => {
                    let dialog = DomBuilder.bootstrapDialogBuilder();
                    dialog.setHeaderContent('Metadata');
                    dialog.setBodyContent([
                        DomBuilder.par(null, completed.metadata)
                    ]);
                    dialog.addCloseButton('Close');
                    dialog.show(root);
                })
            ];
        }

        function actionsColumn(completed) {
            return DomBuilder.link('Delete', null, () => {
                handle(false, true, fetch('/api/admin/completed/' + completed.id, {
                    method: 'DELETE',
                }), result => {
                    refreshCompleted();
                });
            })
        }

        table.addRow(DomBuilder.element('tr', null, [
            DomBuilder.element('th', null, 'ID'),
            DomBuilder.element('th', null, 'Date'),
            DomBuilder.element('th', null, 'Scenario'),
            DomBuilder.element('th', null, 'Metadata'),
            DomBuilder.element('th', null, 'Snapshot'),
            DomBuilder.element('th', null, 'IP'),
            DomBuilder.element('th', null, '_'),
        ]));

        for (const completed of completedList) {
            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('td', null, '' + completed.id),
                DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(completed.time)), {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, DomBuilder.span(completed.scenario, {
                    style: 'white-space: nowrap;'
                })),
                DomBuilder.element('td', null, metadataColumn(completed)),
                DomBuilder.element('td', null, snapshotColumn(completed)),
                DomBuilder.element('td', null, completed.ip),
                DomBuilder.element('td', null, actionsColumn(completed)),
            ]));
        }

        completedDiv.innerHTML = '';
        completedDiv.append(table.createNode());
    });
}

refreshCompleted();
