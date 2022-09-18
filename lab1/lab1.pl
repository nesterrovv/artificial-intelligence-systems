/*
Династия Виндзоров
*/

/*
Оператор приказывает игнорировать best practice SWI-Prolog-а, запрещающий размещать 
факты разного типа несгрупированными.
*/
:- discontiguous 
    male/1, female/1, 
    married/2, husband/2, wife/2, 
    parent/2, father/2,  mother/2, child/2, son/2, daughter/2,
    sibling/2, brother/2, sister/2,
    grandparent/2, grandfather/2, grandmother/2,
    grandchild/2, grandson/2, granddaughter/2,
    auntOrUncle/2, uncle/2, aunt/2.

% rules
married(Husband, Wife) :- Husband @< Wife, married(Wife, Husband).                                              % супруги
% @< нужно для отключения бесконечной рекурсии из-за постоянного переставления аргументов
husband(Husband, Wife) :- male(Husband), married(Husband, Wife).                                                % муж
wife(Wife, Husband) :- female(Wife), married(Wife, Husband).                                                    % жена
father(Father, Child) :- male(Father), parent(Father, Child).                                                   % отец
mother(Mother, Child) :- female(Mother), parent(Mother, Child).                                                 % мать
child(Child, Parent) :- parent(Parent, Child).                                                                  % ребёнок
son(Child, Parent) :- male(Child), child(Child, Parent).                                                        % сын
daughter(Child, Parent) :- female(Child), child(Child, Parent).                                                 % дочь
sibling(Child1, Child2) :- 
                            father(Father, Child1), father(Parent, Child2),
                            mother(Mother, Child1), mother(Mother, Child2),
                            Child1 \= Child2.                                                                   % брат или сестра (саблинги)
brother(Brother, Child) :- male(Brother), sibling(Brother, Child).                                              % брат
sister(Sister, Child) :- female(Sister), sibling(Sister, Child).                                                % сестра
grandparent(Grandp, Child) :- parent(Grandp, Parent),  parent(Parent, Child).                                   % прародители
grandfather(Grandf, Child) :- male(Grandf), grandparent(Grandf, Child).                                         % дедушка 
grandmother(Grandm, Child) :- female(Grandm), grandparent(Grandm, Child).                                       % бабушка
grandchild(Child, Grandp) :- grandparent(Grandp, Child).                                                        % внук или внучка
grandson(Child, Grandp) :- male(Child), grandchild(Child, Grandp).                                              % внук
granddaughter(Child, Grandp) :- female(Child), grandchild(Child, Grandp).                                       % внучка
auntOrUncle(ParentSibling1, Child) :- sibling(ParentSibling1, ParentSibling2), parent(ParentSibling2, Child).   % дядя или тётя
uncle(Uncle, Child) :- male(Uncle), auntoruncle(Uncle, Child).                                                  % дядя
aunt(Aunt, Child) :- female(Aunt), auntoruncle(Aunt, Child).                                                    % тётя

% facts
male('Георг V').

male('Эдуард VIII').
parent('Георг V', 'Эдуард VIII').

male('Георг VI').
parent('Георг V', 'Георг VI').

male('принц Генри, герцог Глостерский').
parent('Георг V', 'принц Генри, герцог Глостерский').

male('принц Георг, герцог Кентский').
parent('Георг V', 'принц Георг, герцог Кентский').

female('Елизавета II').
parent('Георг VI', 'Елизавета II').
male('Филипп, герцог Эдинбургский').
married('Филипп, герцог Эдинбургский', 'Елизавета II').
male('Карл III').
parent('Филипп, герцог Эдинбургский', 'Карл III').
parent('Елизавета II', 'Карл III').
female('принцесса Анна').
parent('Филипп, герцог Эдинбургский', 'принцесса Анна').
parent('Елизавета II', 'принцесса Анна').
male('принц Эндрю, герцог Йоркский').
parent('Филипп, герцог Эдинбургский', 'принц Эндрю, герцог Йоркский').
parent('Елизавета II', 'принц Эндрю, герцог Йоркский').
male('принц Эдвард, граф Уэссекский').
parent('Филипп, герцог Эдинбургский', 'принц Эдвард, граф Уэссекский').
parent('Елизавета II', 'принц Эдвард, граф Уэссекский').

male('принц Ричард, герцог Глостерский').
female('Биргитта, герцогиня Глостерская').
married('принц Ричард, герцог Глостерский', 'Биргитта, герцогиня Глостерская').

male('принц Эдвард, герцог Кентский').
female('Принцесса Александра, достопочтенная леди Огилви').
male('принц Майкл Кентский').

parent('принц Георг, герцог Кентский', 'принц Эдвард, герцог Кентский').
parent('принц Георг, герцог Кентский', 'Принцесса Александра, достопочтенная леди Огилви').
parent('принц Георг, герцог Кентский', 'принц Майкл Кентский').

female('Екатерина, герцогиня Кентская').
married('принц Эдвард, герцог Кентский', 'Екатерина, герцогиня Кентская').

female('Мари Кристина фон Рейбниц, принцесса Кентская').
married('принц Эдвард, герцог Кентский', 'Мари Кристина фон Рейбниц, принцесса Кентская').

female('принцесса Беатриса Йоркская').
female('принцесса Евгения Йоркская').
parent('принц Эндрю, герцог Йоркский', 'принцесса Беатриса Йоркская').
parent('принц Эндрю, герцог Йоркский', 'принцесса Евгения Йоркская').

female('Софи, графиня Уэссекская').
married('принц Эдвард, граф Уэссекский', 'Софи, графиня Уэссекская').
male('Джеймс, виконт Северн').
female('леди Луиза Виндзор').
parent('принц Эдвард, граф Уэссекский', 'Джеймс, виконт Северн').
parent('Софи, графиня Уэссекская', 'Джеймс, виконт Северн').
parent('принц Эдвард, граф Уэссекский', 'леди Луиза Виндзор').
parent('Софи, графиня Уэссекская', 'леди Луиза Виндзор').

female('Камилла').
married('Карл III', 'принцесса Диана').
married('Карл III', 'Камилла').
male('принц Уильям, принц Уэльский').
parent('Карл III', 'принц Уильям, принц Уэльский').
parent('Принцесса Диана', 'принц Уильям, принц Уэльский').
female('Кэтрин, принцесса Уэльская').
married('принц Уильям, принц Уэльский', 'Кэтрин, принцесса Уэльская').
male('принц Джордж Уэльский').
male('принц Луи Уэльский').
female('принцесса Шарлотта Уэльская').
parent('принц Уильям, принц Уэльский', 'принц Джордж Уэльский').
parent('Кэтрин, принцесса Уэльская', 'принц Джордж Уэльский').
parent('принц Уильям, принц Уэльский', 'принц Луи Уэльский').
parent('Кэтрин, принцесса Уэльская', 'принц Луи Уэльский').
parent('принц Уильям, принц Уэльский', 'принцесса Шарлотта Уэльская').
parent('Кэтрин, принцесса Уэльская', 'принцесса Шарлотта Уэльская').
male('принц Гарри, герцог Сассекский').
parent('Карл III', 'принц Гарри, герцог Сассекский').
parent('Принцесса Диана', 'принц Гарри, герцог Сассекский').
female('Меган, герцогиня Сассекская').
married('принц Гарри, герцог Сассекский', 'Меган, герцогиня Сассекская').
male('Арчи Маунтбеттен-Виндзор').
female('Лилибет Маунтбеттен-Виндзор').
parent('принц Гарри, герцог Сассекский', 'Арчи Маунтбеттен-Виндзор').
parent('Меган, герцогиня Сассекская', 'Арчи Маунтбеттен-Виндзор').
parent('принц Гарри, герцог Сассекский', 'Лилибет Маунтбеттен-Виндзор').
parent('Меган, герцогиня Сассекская', 'Лилибет Маунтбеттен-Виндзор').
