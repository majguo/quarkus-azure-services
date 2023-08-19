# Quarkiverse - Quarkus Azure Services

[![Build](https://github.com/quarkiverse/quarkus-azure-services/workflows/Build/badge.svg?branch=main)](https://github.com/quarkiverse/quarkus-azure-services/actions?query=workflow%3ABuild)
[![License](https://img.shields.io/github/license/quarkiverse/quarkus-azure-services.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Central](https://img.shields.io/maven-central/v/io.quarkiverse.azureservices/quarkus-azure-services-parent?color=green)](https://central.sonatype.com/artifact/io.quarkiverse.azureservices/quarkus-azure-services-parent)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-11-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

This repository hosts Quarkus extensions for different Azure Services. You can check
the [documentation of these services](https://docs.quarkiverse.io/quarkus-azure-services/dev/index.html).

## Azure Services

The following extensions allows you to interact with some of the Azure Services:

- [Quarkus Azure App Configuration Extension](https://docs.quarkiverse.io/quarkus-azure-services/dev/quarkus-azure-app-configuration.html): [Azure App Configuration](https://azure.microsoft.com/products/app-configuration)
  is a fast, scalable parameter storage for app configuration.
- [Quarkus Azure Storage Blob Extension](https://docs.quarkiverse.io/quarkus-azure-services/dev/quarkus-azure-storage-blob.html):
  [Azure Storage Blob](https://azure.microsoft.com/products/storage/blobs/)
  is a massively scalable and secure object storage for cloud-native workloads, archives, data lakes, high-performance
  computing, and machine learning.
- [Quarkus Azure Storage Queue Extension](https://docs.quarkiverse.io/quarkus-azure-services/dev/quarkus-azure-storage-queue.html):
  [Azure Storage Queue](https://azure.microsoft.com/products/storage/blobs/)
  is a durable queue for large-volume cloud services providing an ability to asynchronously communicate between different
  application components and a simple and reliable way to exchange messages between different parts of a distributed systems.

## Example applications

Example applications can be found inside the [integration-tests](integration-tests) folder:

- [Azure App Configuration sample](integration-tests/azure-app-configuration): REST endpoint using the Quarkus extension
  to get the configuration stored in Azure App Configuration.
- [Azure Storage Blob sample](integration-tests/azure-storage-blob): REST endpoint using the Quarkus extension to
  upload and download files to/from Azure Storage Blob.
- [Azure Storage Queue sample](integration-tests/azure-storage-queue): REST endpoint using the Quarkus extension to
  post and receive messages to/from Azure Storage Queue.

## How to release a new version

Follow [this wiki](https://github.com/quarkiverse/quarkiverse/wiki/Release) to release a new version of the extensions.
You can reference the following PRs as examples:

* Release a new version: https://github.com/quarkiverse/quarkus-azure-services/pull/133
* Register new extensions in catalog: https://github.com/quarkusio/quarkus-extension-catalog/pull/64.
  See [Publish your extension in registry.quarkus.io](https://quarkus.io/guides/writing-extensions#publish-your-extension-in-registry-quarkus-io)
  for more information.

## Contributing

Contributions are always welcome, but better create an issue to discuss them prior to any contributions.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://www.linkedin.com/in/jianguo-ma-40783518/"><img src="https://avatars.githubusercontent.com/u/10357495?v=4?s=100" width="100px;" alt="Jianguo Ma"/><br /><sub><b>Jianguo Ma</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=majguo" title="Code">💻</a> <a href="#maintenance-majguo" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://www.antoniogoncalves.org"><img src="https://avatars.githubusercontent.com/u/729277?v=4?s=100" width="100px;" alt="Antonio Goncalves"/><br /><sub><b>Antonio Goncalves</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=agoncal" title="Code">💻</a> <a href="#maintenance-agoncal" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://www.radcortez.com"><img src="https://avatars.githubusercontent.com/u/5796305?v=4?s=100" width="100px;" alt="Roberto Cortez"/><br /><sub><b>Roberto Cortez</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=radcortez" title="Code">💻</a> <a href="https://github.com/quarkiverse/quarkus-azure-services/pulls?q=is%3Apr+reviewed-by%3Aradcortez" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://twitter.com/ppalaga"><img src="https://avatars.githubusercontent.com/u/1826249?v=4?s=100" width="100px;" alt="Peter Palaga"/><br /><sub><b>Peter Palaga</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=ppalaga" title="Code">💻</a> <a href="https://github.com/quarkiverse/quarkus-azure-services/pulls?q=is%3Apr+reviewed-by%3Appalaga" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://lesincroyableslivres.fr/"><img src="https://avatars.githubusercontent.com/u/1279749?v=4?s=100" width="100px;" alt="Guillaume Smet"/><br /><sub><b>Guillaume Smet</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=gsmet" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://gastaldi.wordpress.com"><img src="https://avatars.githubusercontent.com/u/54133?v=4?s=100" width="100px;" alt="George Gastaldi"/><br /><sub><b>George Gastaldi</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=gastaldi" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/JoaoBrandao"><img src="https://avatars.githubusercontent.com/u/13374459?v=4?s=100" width="100px;" alt="João Brandão"/><br /><sub><b>João Brandão</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/issues?q=author%3AJoaoBrandao" title="Bug reports">🐛</a></td>
    </tr>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="http://melloware.com"><img src="https://avatars.githubusercontent.com/u/4399574?v=4?s=100" width="100px;" alt="Melloware"/><br /><sub><b>Melloware</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/issues?q=author%3Amelloware" title="Bug reports">🐛</a> <a href="https://github.com/quarkiverse/quarkus-azure-services/pulls?q=is%3Apr+reviewed-by%3Amelloware" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://ridingthecrest.com/"><img src="https://avatars.githubusercontent.com/u/75821?v=4?s=100" width="100px;" alt="Ed Burns"/><br /><sub><b>Ed Burns</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/pulls?q=is%3Apr+reviewed-by%3Aedburns" title="Reviewed Pull Requests">👀</a> <a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=edburns" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/backwind1233"><img src="https://avatars.githubusercontent.com/u/4465723?v=4?s=100" width="100px;" alt="zhihaoguo"/><br /><sub><b>zhihaoguo</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/pulls?q=is%3Apr+reviewed-by%3Abackwind1233" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://thejavaguy.org/"><img src="https://avatars.githubusercontent.com/u/11942401?v=4?s=100" width="100px;" alt="Ivan Milosavljević"/><br /><sub><b>Ivan Milosavljević</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-azure-services/commits?author=TheJavaGuy" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification.
Contributions of any kind welcome!
