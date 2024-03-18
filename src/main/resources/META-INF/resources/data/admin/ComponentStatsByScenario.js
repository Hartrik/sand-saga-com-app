import Component from "./Component.js";
import DomBuilder from "./DomBuilder.js";
import { handle } from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */
export default class ComponentStatsByScenario extends Component {

    #node = DomBuilder.div();

    refresh() {
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
                DomBuilder.element('th', null, 'Completed'),
            ]));

            scenarios = scenarios.sort((a, b) => a.name.localeCompare(b.name));
            for (const stats of scenarios) {
                table.addRow(DomBuilder.element('tr', null, [
                    DomBuilder.element('td', null, '' + stats.id),
                    DomBuilder.element('td', null, stats.name),
                    DomBuilder.element('td', null, '' + stats.updates),
                    DomBuilder.element('td', null, '' + stats.completed),
                ]));
            }

            const total = DomBuilder.par();

            fetch('/api/admin/stats/by-scenario/sum', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json()).then(stats => {
                total.append(DomBuilder.span(`Total updates: ${stats.updates}`));
                total.append(DomBuilder.element('br'));
                total.append(DomBuilder.span(`Total completed: ${stats.completed}`));
            });

            this.#node.innerHTML = '';
            this.#node.append(table.createNode());
            this.#node.append(total);
        });
    }

    createNode() {
        return this.#node;
    }
}