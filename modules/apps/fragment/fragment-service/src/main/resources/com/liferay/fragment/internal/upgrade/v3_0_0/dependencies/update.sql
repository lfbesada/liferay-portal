create table FragmentEntryPropagation (
	  mvccVersion LONG default 0 not null,
	  ctCollectionId LONG default 0 not null,
	  fragmentEntryPropagationId LONG not null,
	  createDate DATE null,
	  modifiedDate DATE null,
	  fragmentEntryKey VARCHAR(75) null,
	  css TEXT null,
	  html TEXT null,
	  js TEXT null,
	  configuration TEXT null,
	  type_ INTEGER,
	  primary key (fragmentEntryPropagationId, ctCollectionId)
);

create unique index IX_A685ADF on FragmentEntryPropagation (fragmentEntryKey[$COLUMN_LENGTH:75$], ctCollectionId);